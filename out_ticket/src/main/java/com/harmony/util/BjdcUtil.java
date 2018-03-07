package com.harmony.util;

import com.harmony.entity.ChipinTempDTO;
import com.harmony.entity.Ticket;
import com.harmony.exception.CheckTicketException;
import com.harmony.exception.SystemEroorException;
import com.harmony.service.ServiceFactory;
import com.harmony.service.impl.ChipinTempService;
import com.harmony.service.impl.SystemExceptionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 2017/8/14 11:43.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 北京单场工具类
 */
public class BjdcUtil {

    private static Logger logger = Logger.getLogger(BjdcUtil.class);

    //北京单场游戏头
    private static String game_head = " 4 1";

    //保存北京单场gamecode对应的票机游戏按键
    private static Map<String, String> bjdc_manner_key = new HashMap<>(7);

    //保存游戏名和游戏玩法
    private static Map<String, String> bjdc_gamename_to_gamecode = new HashMap<>(7);

    //保存上下单双按键
    private static Map<String, String> bjdc_sxds_key = new HashMap<>(4);

    //保存玩法对应的串关方式
    private static Map<String, String> bjdc_manner_to_name = new HashMap<>(30);

    //用于转换胜负过关306 的按键码为 胜 负
    private static Map<String, String> bjdc_sfgg_key_to_name = new HashMap<>(2);

    //过关方式
    public static final Map<String, String> BD_GGFS_MAP = new HashMap<String, String>();

    //过关方式
    public static final Map<Integer, String> BD_GGFS_MAP_MANNER1 = new HashMap<Integer, String>();

    static {

        /************按键值map前面都有空格**************/
        //过关方式
        BD_GGFS_MAP_MANNER1.put(1, "0 1");
        BD_GGFS_MAP_MANNER1.put(2, "0 3");
        BD_GGFS_MAP_MANNER1.put(3, "0 5");
        BD_GGFS_MAP_MANNER1.put(4, "0 7");
        BD_GGFS_MAP_MANNER1.put(5, "0 8");
        BD_GGFS_MAP_MANNER1.put(6, "0 9");
        BD_GGFS_MAP_MANNER1.put(7, "0 4");
        BD_GGFS_MAP_MANNER1.put(8, "0 4");
        BD_GGFS_MAP_MANNER1.put(9, "0 4");
        BD_GGFS_MAP_MANNER1.put(10, "0 4");
        BD_GGFS_MAP_MANNER1.put(11, "0 4");
        BD_GGFS_MAP_MANNER1.put(12, "0 4");
        BD_GGFS_MAP_MANNER1.put(13, "0 4");
        BD_GGFS_MAP_MANNER1.put(14, "0 4");
        BD_GGFS_MAP_MANNER1.put(15, "0 4");

        /************按键值map前面都有空格**************/
        //过关方式
        BD_GGFS_MAP.put("1", "0 1");
        BD_GGFS_MAP.put("2", "0 1");
        BD_GGFS_MAP.put("3", "0 2");
        BD_GGFS_MAP.put("4", "0 1");
        BD_GGFS_MAP.put("5", "0 2");
        BD_GGFS_MAP.put("6", "0 3");
        BD_GGFS_MAP.put("7", "0 1");
        BD_GGFS_MAP.put("8", "0 2");
        BD_GGFS_MAP.put("9", "0 3");
        BD_GGFS_MAP.put("10", "0 4");
        BD_GGFS_MAP.put("11", "0 1");
        BD_GGFS_MAP.put("12", "0 2");
        BD_GGFS_MAP.put("13", "0 3");
        BD_GGFS_MAP.put("14", "0 4");
        BD_GGFS_MAP.put("15", "0 5");
        BD_GGFS_MAP.put("16", "0 1");
        BD_GGFS_MAP.put("17", "0 2");
        BD_GGFS_MAP.put("18", "0 3");
        BD_GGFS_MAP.put("19", "0 4");
        BD_GGFS_MAP.put("20", "0 5");
        BD_GGFS_MAP.put("21", "0 6");
        BD_GGFS_MAP.put("22", "0 1");
        BD_GGFS_MAP.put("23", "0 1");
        BD_GGFS_MAP.put("24", "0 1");
        BD_GGFS_MAP.put("25", "0 1");
        BD_GGFS_MAP.put("26", "0 1");
        BD_GGFS_MAP.put("27", "0 1");
        BD_GGFS_MAP.put("28", "0 1");
        BD_GGFS_MAP.put("29", "0 1");
        BD_GGFS_MAP.put("30", "0 1");

        bjdc_manner_key.put("301", " 4 1");
        bjdc_manner_key.put("302", " 4 2");
        bjdc_manner_key.put("303", " 4 3");
        bjdc_manner_key.put("304", " 4 5");
        bjdc_manner_key.put("305", " 4 4");
        bjdc_manner_key.put("306", " 4 7");
        bjdc_manner_key.put("307", " 4 6");

        bjdc_sxds_key.put("上单", " 1");
        bjdc_sxds_key.put("上双", " 2");
        bjdc_sxds_key.put("下单", " 3");
        bjdc_sxds_key.put("下双", " 4");

        bjdc_gamename_to_gamecode.put("胜平负", "301");
        bjdc_gamename_to_gamecode.put("上下盘单双数", "302");
        //下面的票还没有确认标题
        bjdc_gamename_to_gamecode.put("总进球", "303");
        bjdc_gamename_to_gamecode.put("单场比分", "304");
        bjdc_gamename_to_gamecode.put("半全场胜平负", "305");
        bjdc_gamename_to_gamecode.put("胜负过关", "306");
        bjdc_gamename_to_gamecode.put("下半场比分", "307");

        bjdc_manner_to_name.put("1", "单关");
        bjdc_manner_to_name.put("2", "2串1");
        bjdc_manner_to_name.put("3", "2串3");
        bjdc_manner_to_name.put("4", "3串1");
        bjdc_manner_to_name.put("5", "3串4");
        bjdc_manner_to_name.put("6", "3串7");
        bjdc_manner_to_name.put("7", "4串1");
        bjdc_manner_to_name.put("8", "4串5");
        bjdc_manner_to_name.put("9", "4串11");
        bjdc_manner_to_name.put("10", "4串15");
        bjdc_manner_to_name.put("11", "5串1");
        bjdc_manner_to_name.put("12", "5串6");
        bjdc_manner_to_name.put("13", "5串16");
        bjdc_manner_to_name.put("14", "5串26");
        bjdc_manner_to_name.put("15", "5串31");
        bjdc_manner_to_name.put("16", "6串1");
        bjdc_manner_to_name.put("17", "6串7");
        bjdc_manner_to_name.put("18", "6串22");
        bjdc_manner_to_name.put("19", "6串42");
        bjdc_manner_to_name.put("20", "6串57");
        bjdc_manner_to_name.put("21", "6串63");
        bjdc_manner_to_name.put("22", "7串1");
        bjdc_manner_to_name.put("23", "8串1");
        bjdc_manner_to_name.put("24", "9串1");
        bjdc_manner_to_name.put("25", "10串1");
        bjdc_manner_to_name.put("26", "11串1");
        bjdc_manner_to_name.put("27", "12串1");
        bjdc_manner_to_name.put("28", "13串1");
        bjdc_manner_to_name.put("29", "14串1");
        bjdc_manner_to_name.put("30", "15串1");

        bjdc_sfgg_key_to_name.put("3", "胜");
        bjdc_sfgg_key_to_name.put("0", "负");
    }

    /**
     * 游戏对应按键
     *
     * @param gameCode
     * @return
     */
    private static String getMannerKey(String gameCode) {
        return bjdc_manner_key.get(gameCode);
    }

    /**
     * 上下单双玩法按键
     *
     * @param sxds
     * @return
     */
    public static String getSxdsKey(String sxds) {
        return bjdc_sxds_key.get(sxds);
    }

    /**
     * 比分玩法按键
     *
     * @param bf
     * @return
     */
    private static String getBfKey(String bf) {
        String result = bf;
        if ("胜其它".equals(bf)) {
            result = "9:0";
        } else if ("平其它".equals(bf)) {
            result = "9:9";
        } else if ("负其它".equals(bf)) {
            result = "0:9";
        }
        result = result.replaceAll(":", " ");
        return result;
    }

    /**
     * 获取总进球按键
     *
     * @param goals
     * @return
     */
    private static String getSumGoals(String goals) {
        if (StringUtils.isEmpty(goals)) {
            logger.info("总进球数不能为空...");
            throw new RuntimeException("总进球数不能为空...");
        }
        int goal = Integer.parseInt(goals);
        return goal >= 7 ? "7" : goals;
    }

    /**
     * 半全场玩法按键
     *
     * @return
     */
    private static String getBqcKey(String bqc) {
        return bqc.replaceAll("-", " ");
    }

    /**
     * 获取每张票的投注按键
     *
     * @param chipinTempDTO
     * @return
     */
    public static String getAllKey(ChipinTempDTO chipinTempDTO) {
        String chipinNums = chipinTempDTO.getChipinNums();
        String gameCode = chipinTempDTO.getGameCode() + "";
        StringBuilder sb = new StringBuilder(game_head);
        sb.append(getMannerKey(gameCode));
        if (StringUtils.isNotEmpty(chipinNums)) {
            String[] everyBets = chipinNums.split("//");
            for (String everyBet : everyBets) {
                String[] temps = everyBet.split(",");
                String number = addZero(temps[0], 3, "0");//场次编号
                sb.append(" ").append(PrintUtil.plusTrim(number));//场次
                String[] playWays = temps[1].split("/");
                for (String playWay : playWays) {
                    if ("301".equals(gameCode)) {
                        sb.append(" ").append(playWay);
                    } else if ("302".equals(gameCode)) {
                        sb.append(getSxdsKey(playWay));
                    } else if ("303".equals(gameCode)) {
                        sb.append(" ").append(getSumGoals(playWay));
                    } else if ("304".equals(gameCode)) {
                        sb.append(" ").append(getBfKey(playWay));
                    } else if ("305".equals(gameCode)) {
                        sb.append(" ").append(getBqcKey(playWay));
                    } else if ("306".equals(gameCode)) {
                        sb.append(" ").append(playWay);
                    } else if ("307".equals(gameCode)) {
                        logger.info("游戏307还没有增加 下半场比分玩法...");
                        //sb.append(" ").append(getBfKey(playWay));
                    }

                }
                sb.append(" " + "F1");
            }

            if (chipinTempDTO.getManner().equals("1")) {
                String[] chim = chipinTempDTO.getChipinNums().split("//");

                if (chim.length == 1) {

                } else {
                    sb.append(" F2 ");
                    sb.append(BD_GGFS_MAP_MANNER1.get(chim.length));
                }
            } else {
                sb.append(" F2 ");
                sb.append(BD_GGFS_MAP.get(chipinTempDTO.getManner()));
            }

        } else {
            logger.info(chipinTempDTO.getTempId() + "投注号码串为空.....");
            throw new RuntimeException(chipinTempDTO.getTempId() + "投注号码串为空.....");
        }
        logger.info(chipinTempDTO.getTempId() + "的投注转换为：" + sb.toString().trim());
        return sb.toString().trim();
    }

    /**
     * 获取投注号码按键命令
     *
     * @param chipinTempDTO
     * @return
     */
    public static byte[] formatResult(ChipinTempDTO chipinTempDTO) {
        String number = getAllKey(chipinTempDTO);
        LogUtil.info(TicketCTFormat.class, "获取键盘码", "键盘码为:" + number);
        return KeyUtil.getKeyCodesByKeys(number);
    }

    /**
     * 获取北单 出票信息
     *
     * @param ticket
     * @param tempByte
     */
    public static String[] getBdPrintInfo(Ticket ticket, byte[] tempByte) throws SystemEroorException {
        String[] info = new String[10];
        try {
            ByteBuffer byteBuffer = null;
            StringBuffer stringBuffer = null;
            byteBuffer = ByteBuffer.allocateDirect(186140);
            stringBuffer = new StringBuffer(186140);
            Charset charset = Charset.forName("GBK");
            CharBuffer charBuffer = charset.decode(byteBuffer.wrap(tempByte));
            stringBuffer.append(charBuffer);

            String msg = stringBuffer.toString();
            //String msg = stringBuffer.toString().replaceAll("\r", "").replaceAll("\n", "");
            logger.info("票机返回票面信息：" + msg);

            //匹配玩法
            Pattern mannerReg = Pattern.compile("([0-9]+串[0-9]+)");
            Matcher mannerMatcher = mannerReg.matcher(msg);
            if (mannerMatcher.find()) {
                info[0] = mannerMatcher.group(1);
            } else {
                mannerReg = Pattern.compile("(单关)");
                mannerMatcher = mannerReg.matcher(msg);
                if (mannerMatcher.find()) {
                    info[0] = mannerMatcher.group(1);
                }
            }
            //匹配时间
            Pattern timeReg = Pattern.compile("时间([0-9]{4}-[0-9]{2}-[0-9]{2}\\s*[0-9]{2}:[0-9]{2}:[0-9]{2})");
            Matcher timeMatcher = timeReg.matcher(msg);
            if (timeMatcher.find()) {
                info[1] = timeMatcher.group(1);
            }
            //匹配期号
            Pattern issueReg = Pattern.compile("第([0-9]+)期");
            Matcher issueMatcher = issueReg.matcher(msg);
            if (issueMatcher.find()) {
                info[2] = issueMatcher.group(1);
            }
            //共多少期
            Pattern sumIssueReg = Pattern.compile("共\\s*([0-9]+)\\s*期");
            Matcher sumIssueMatcher = sumIssueReg.matcher(msg);
            if (sumIssueMatcher.find()) {
                info[3] = sumIssueMatcher.group(1);
            }
            //倍数
            Pattern multReg = Pattern.compile("\\s*([0-9]+)\\s*倍");
            Matcher mulMatcher = multReg.matcher(msg);
            if (mulMatcher.find()) {
                info[4] = mulMatcher.group(1);
            }
            //总金额
            Pattern moneyReg = Pattern.compile("\\s*([0-9]+)\\s*元");
            Matcher moneyMatcher = moneyReg.matcher(msg);
            if (moneyMatcher.find()) {
                info[5] = moneyMatcher.group(1);
            }
            //落地号
            Pattern ldhReg = Pattern.compile("([0-9]{6}-[0-9]{6}-[0-9]{6}-[0-9]{6})\\s*");
            Matcher ldhMatcher = ldhReg.matcher(msg);
            if (ldhMatcher.find()) {
                info[6] = ldhMatcher.group(1);
            }
            //密码
            Pattern passReg = Pattern.compile("\\s*([a-zA-Z0-9]{13})\\s*");
            Matcher passMatcher = passReg.matcher(msg);
            if (passMatcher.find()) {
                info[7] = passMatcher.group(1);
            }
            //游戏
            Pattern gameReg = Pattern.compile("\\s*\\[([\\u4e00-\\u9fa5]+)\\]\\s*");
            Matcher gameMatcher = gameReg.matcher(msg);
            if (gameMatcher.find()) {
                info[8] = gameMatcher.group(1);
            }

            //保存投注内容
            StringBuilder sb = new StringBuilder();
            //场次匹配
            Pattern cc = null;
            Matcher ccMatcher = null;
            //匹配投注内容
            if (ticket != null && StringUtils.isNotEmpty(ticket.getGameCode())) {
                if ("301".equals(ticket.getGameCode())) {
                    cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\s*((-\\s*)*([0-9]\\s*)*(-\\s*)*)([\\n\\[])");
                } else if ("302".equals(ticket.getGameCode())) {
                    cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\s*((上单\\s*)*(——\\s*)*(上双\\s*)*(——\\s*)*(下单\\s*)*(——\\s*)*(下双\\s*)*)");
                } else if ("303".equals(ticket.getGameCode())) {
                    cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\s*(([0-7]\\+*\\s*)*(-\\s*)*([0-7]\\+*\\s*)*(-\\s*)*([0-7]\\+*\\s*)*(-\\s*)*([0-7]\\+*\\s*)*(-\\s*)*([0-7]\\+*\\s*)*)\\n");
                } else if ("304".equals(ticket.getGameCode())) {
                    cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\)\\s*\\n((([0-9]\\s*:[0-9]\\s*)+|(-\\s*)+|(\\n)+)+)");
                } else if ("305".equals(ticket.getGameCode())) {
                    //cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\)\\s*\\n(([0-9]-[0-9]\\s*)*(-\\s*)*([0-9]-[0-9]\\s*)*(-\\s*)*([0-9]-[0-9]\\s*)*(-\\s*)*([0-9]-[0-9]\\s*)*(-\\s*)*([0-9]-[0-9]\\s*)*)");
                    cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\)\\s*\\n(((-\\s*){3,}|([0-9]-[0-9]\\s*)|\\n+)+)");
                } else if ("306".equals(ticket.getGameCode())) {
                    //cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\s*((胜\\s*)*(负\\s*)*(-\\s*)*(胜\\s*)*(负\\s*)*)([\\[\\n])");
                    cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\s*((((胜\\s*)*|(负\\s*)*)*|(-\\s*)*)+)[\\n\\[]");
                } else if ("307".equals(ticket.getGameCode())) {

                }
                //循环匹配从票机返回的场次信息
                ccMatcher = cc.matcher(msg);
                while (ccMatcher.find()) {
                    String changci = ccMatcher.group(1);//场次
                    String playWays = ccMatcher.group(2);//玩法
                    if (StringUtils.isNotEmpty(changci) && StringUtils.isNotEmpty(playWays) && validePlayWays(playWays)) {
                        String resultPlays = playWaysConvert(ticket.getGameCode(), playWays);
                        sb.append(changci);
                        sb.append(",");
                        sb.append(resultPlays);
                        sb.append("//");
                    }
                }
                if (sb.length() > 2) {
                    sb.deleteCharAt(sb.length() - 1);
                    sb.deleteCharAt(sb.length() - 1);
                    info[9] = sb.toString().trim();
                }
            }

            logger.info("玩法=" + info[0] + "  出票时间=" + info[1] + " 期号=" + info[2] + " 共多少期=" + info[3] + " 倍数=" + info[4] + " 总金额=" + info[5] + " 落地号=" + info[6] + " 密码=" + info[7] + " 游戏=" + info[8] + " 场次=" + info[9]);
        } catch (Exception e) {
            logger.error("split error ====", e);
            throw new SystemEroorException(e);
        }
        return info;
    }

    /**
     * 验校北单
     *
     * @param info
     * @param chipinTempDTO
     * @param ticket
     * @return
     */
    public static boolean checkBd(String[] info, ChipinTempDTO chipinTempDTO, Ticket ticket) throws CheckTicketException, SQLException {
        SystemExceptionService systemExceptionService = (SystemExceptionService) ServiceFactory.getService("systemExceptionService");
        if (!checkVoteContent(info, chipinTempDTO)) {
            systemExceptionService.insert(PrintUtil.formatPrint(ticket, "号码不相符!!!!!实际票面号码:" + info[9]), null);
            throw new CheckTicketException("号码不相符!!!!!");
        }
        if (!info[2].equals(chipinTempDTO.getIssue())) {
            systemExceptionService.insert(PrintUtil.formatPrint(ticket, "期号不相符!!!!!实际票面期号:" + info[2]), null);
            throw new CheckTicketException("期号不相符!!!!");
        }
        if (!info[4].equals(chipinTempDTO.getMultiple())) {
            systemExceptionService.insert(PrintUtil.formatPrint(ticket, "倍数打错!!!!!实际票面倍数:" + info[4]), null);
            throw new CheckTicketException("倍数打错!!!!!");
        }
        if (chipinTempDTO.getBets() - Double.parseDouble(info[5]) != 0) {
            systemExceptionService.insert(PrintUtil.formatPrint(ticket, "金额不相符!!!!!实际票面金额:" + info[5]), null);
            throw new CheckTicketException("金额不相符!!!!!");
        }
        if (!info[0].equals(bjdc_manner_to_name.get(chipinTempDTO.getManner()))) {
            systemExceptionService.insert(PrintUtil.formatPrint(ticket, "玩法不相符!!!!!票机所出票玩法：" + info[0] + "实际票面玩法:" + chipinTempDTO.getManner()), null);
            throw new CheckTicketException("玩法不相符!!!!!");
        }
        return true;
    }

    /**
     * 检测投注内容是否正确
     *
     * @param info
     * @param chipinTempDTO
     * @return
     */
    public static boolean checkVoteContent(String[] info, ChipinTempDTO chipinTempDTO) {
        List<String> listDB = getListByVoteNums(chipinTempDTO.getChipinNums());
        List<String> list = getListByVoteNums(info[9]);
        return listDB.containsAll(list);
    }

    /**
     * 把投注串 分成单个保存到list  109,0
     *
     * @param chipinStr
     * @return
     */
    public static List<String> getListByVoteNums(String chipinStr) {
        List<String> list = new ArrayList<String>();
        String[] chipinArray = chipinStr.split("\\/\\/");
        for (String str : chipinArray) {
            String num = str.substring(0, str.indexOf(","));
            String param = str.substring(str.indexOf(",") + 1, str.length());
            String[] chipin = param.split("\\/");
            int cpLen = chipin.length;
            for (int i = 0; i < cpLen; i++) {
                list.add(num + "," + chipin[i]);
            }
        }
        return list;
    }

    /**
     * 检测必须为有效玩法  --- --- --- --- --- --- --- --- ------ 为无效玩法
     * 必须有 数字 胜 负 上 下 单 双
     *
     * @param playWays
     * @return
     */
    private static String[] validePlayWays = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "胜", "负", "上", "下", "单", "双"};

    public static boolean validePlayWays(String playWays) {
        if (StringUtils.isNotEmpty(playWays)) {
            for (int i = 0; i < playWays.length(); i++) {
                String everyChar = playWays.charAt(i) + "";
                for (String tem : validePlayWays) {
                    if (everyChar.equals(tem)) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    /**
     * 不足长度 前面加0
     *
     * @param source
     * @param length
     * @return
     */

    private static String addZero(String source, int length, String addChar) {
        StringBuilder sb = null;
        if (StringUtils.isNotEmpty(source) && source.length() < length) {
            sb = new StringBuilder();
            for (int i = source.length(); i < length; i++) {
                sb.append(addChar);
            }
            sb.append(source);
        } else {
            sb = new StringBuilder(source);
        }
        return sb.toString();
    }

    /**
     * 转换胜负过关按键码为 胜 负
     * 仅仅对306游戏
     *
     * @param sfggVal
     * @return
     */
    private static String convertSfggToName(String sfggVal) {
        String[] vals = sfggVal.split("/");
        StringBuilder sb = new StringBuilder();
        for (String val : vals) {
            sb.append(bjdc_sfgg_key_to_name.get(val));
            sb.append("/");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 转换玩法 --> 数据库一模一样
     *
     * @param gameCode
     * @param playWays
     * @return
     */
    private static String playWaysConvert(String gameCode, String playWays) {
        StringBuilder sb = new StringBuilder();
        String toTest = playWays.trim();
        if ("301".equals(gameCode)) {//3 0 - 或者 - 3 - 转换为 3/0
            for (int i = 0; i < toTest.length(); i++) {
                String tem = toTest.charAt(i) + "";
                if (StringUtils.isNotEmpty(tem) && !"-".equals(tem) && !" ".equals(tem)) {
                    sb.append(tem);
                    sb.append("/");
                }
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
        } else if ("302".equals(gameCode)) {
            for (int i = 0; i < toTest.length(); i++) {
                String tem = toTest.charAt(i) + "";
                if (StringUtils.isNotEmpty(tem) && !"-".equals(tem) && !" ".equals(tem)) {
                    i++;
                    tem = tem + toTest.charAt(i) + "";//这里取两个汉字
                    if (!"——".equals(tem)) {
                        sb.append(tem);
                        sb.append("/");
                    }

                }
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
        } else if ("303".equals(gameCode)) {
            for (int i = 0; i < toTest.length(); i++) {
                String tem = toTest.charAt(i) + "";
                if (StringUtils.isNotEmpty(tem) && !"-".equals(tem) && !" ".equals(tem) && !"+".equals(tem)) {
                    sb.append(tem);
                    sb.append("/");
                }
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
        } else if ("304".equals(gameCode)) {
            for (int i = 0; i < toTest.length(); i++) {
                String tem = toTest.charAt(i) + "";
                if (StringUtils.isNotEmpty(tem) && !"-".equals(tem) && !" ".equals(tem) && !"\n".equals(tem)) {
                    i++;
                    tem = tem + toTest.charAt(i) + "";
                    i++;
                    tem = tem + toTest.charAt(i) + "";//这里取1:1
                    sb.append(tem);
                    sb.append("/");
                }
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
        } else if ("305".equals(gameCode)) {
            for (int i = 0; i < toTest.length(); i++) {
                String tem = toTest.charAt(i) + "";
                if (StringUtils.isNotEmpty(tem) && !"-".equals(tem) && !" ".equals(tem) && !"\n".equals(tem)) {
                    i++;
                    tem = tem + toTest.charAt(i) + "";
                    i++;
                    tem = tem + toTest.charAt(i) + "";//这里取1-1
                    sb.append(tem);
                    sb.append("/");
                }
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }

        } else if ("306".equals(gameCode)) {//胜3平1负0  例：胜平 -  转换为 3/1
            for (int i = 0; i < toTest.length(); i++) {
                String tem = toTest.charAt(i) + "";
                if (StringUtils.isNotEmpty(tem) && !"-".equals(tem) && !" ".equals(tem)) {
                    if (tem.equals("胜")) {
                        sb.append("3");
                    } else if (tem.equals("平")) {
                        sb.append("1");
                    } else {
                        sb.append("0");
                    }
                    sb.append("/");
                }
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
        } else if ("307".equals(gameCode)) {
            logger.info("游戏307还没有增加 下半场比分玩法...");
            for (int i = 0; i < toTest.length(); i++) {
                String tem = toTest.charAt(i) + "";
                if (StringUtils.isNotEmpty(tem) && !"-".equals(tem) && !" ".equals(tem) && !"\n".equals(tem)) {
                    i++;
                    tem = tem + toTest.charAt(i) + "";
                    i++;
                    tem = tem + toTest.charAt(i) + "";//这里取1:1
                    sb.append(tem);
                    sb.append("/");
                }
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws SQLException {
        ChipinTempService chipinTempService = (ChipinTempService) ServiceFactory.getService("chipinTempService");
        ChipinTempDTO chipinTempDTO = chipinTempService.selectByTempid(1000001448061199L);
        System.out.println(getAllKey(chipinTempDTO));
        System.out.println(playWaysConvert("301", "3 0 -"));
        System.out.println(playWaysConvert("301", "3 - -"));
        System.out.println(playWaysConvert("301", "- 0 -"));
        System.out.println(playWaysConvert("302", "上单 - - 下单 - -"));
        System.out.println(playWaysConvert("302", "----下单 - -"));
        System.out.println(playWaysConvert("303", "----- 9 - 0 -"));
        System.out.println(playWaysConvert("303", "2-- - - - - 0 -"));
        System.out.println(playWaysConvert("305", "1-2 --- --- --- --- --- --- ---"));
        System.out.println(playWaysConvert("305", "1-2 --- --- --- --- --- --- 3-4"));
        System.out.println(playWaysConvert("305", "--- 3-1 --- --- --- --- 4-5 ---"));
        System.out.println(playWaysConvert("306", "胜 --"));
        System.out.println(playWaysConvert("306", "胜 平"));

        List<String> ls1 = getListByVoteNums("301,1:2/2:3//302,2:1/2:2//303,3:3");
        List<String> ls2 = getListByVoteNums("301,1:2/2:3//302,2:1/2:2//303,3:3");
        System.out.println(ls1.containsAll(ls2));

    }

}
