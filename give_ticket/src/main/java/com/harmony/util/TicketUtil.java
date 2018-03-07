package com.harmony.util;

import com.harmony.entity.*;
import com.harmony.exception.CheckTicketException;
import com.harmony.exception.SystemEroorException;
import com.harmony.service.ServiceFactory;
import com.harmony.service.impl.SystemExceptionService;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2017/9/22 16:12.
 * <p>
 * Email: cheerUpPing@163.com
 */
public class TicketUtil {

    /**
     * 获取竞彩 出票信息
     *
     * @param ticket
     * @param ticketInfo
     * @return 落地号，校验码，密码, 黑标, 赔率, 倍数，金额
     */
    public static String[] getJCPrintInfo(ChipinTempDTO ticket, String ticketInfo) {
        String[] info = new String[9];//第九个保存玩法

        try {
            String msg = ticketInfo.replaceAll("\r", "").replaceAll("\n", "");
            String msg_manner = ticketInfo.replaceAll("\r", "  ").replaceAll("\n", " ");

            //匹配玩法[竞彩]
            Pattern mannerReg = Pattern.compile("\\d+场-((\\d,)*\\d+)关");
            Matcher mannerMatcher = mannerReg.matcher(msg_manner);
            Pattern mannerSignleReg = Pattern.compile("\\d+场-单场固定");
            Matcher mannerSignleMatcher = mannerSignleReg.matcher(msg_manner);
            String manner = "";
            //是自有过关
            if (mannerMatcher.find()) {
                manner = mannerMatcher.group(1);
            } else if (mannerSignleMatcher.find()) {
                manner = "1";//单场固定
            } else {//传统过关
                mannerReg = Pattern.compile("(\\d+x\\d+)");
                mannerMatcher = mannerReg.matcher(msg_manner);
                if (mannerMatcher.find()) {
                    manner = mannerMatcher.group(1);
                }
            }
            info[8] = manner;

            Pattern p = Pattern.compile("([0-9]{6}\\-[0-9]{6}\\-[0-9]{6}\\-[0-9]{6})\\s+([0-9]{6})");
            Matcher m = p.matcher(msg);

            if (m.find()) {
                //落地号格式[110217-468700-000201-562708]新票机传统格式
                info[0] = m.group(1);
                info[2] = m.group(2);

                //找倍数
                Pattern multiplePattern = Pattern.compile("([0-9]+)\\S*\\s*倍");
                Matcher multipleMatcher = multiplePattern.matcher(msg);
                Pattern multiplePattern1 = Pattern.compile("追加投注([0-9]+)倍");
                Matcher multipleMatcher1 = multiplePattern1.matcher(msg);

                if (multipleMatcher.find()) {
                    info[5] = multipleMatcher.group(1);
                } else if (multipleMatcher1.find()) {
                    info[5] = multipleMatcher1.group(1);
                }
                Pattern totalPattern = Pattern.compile("合计[\\S*\\s]*PrinterPrintString\\x00str\\s*([0-9]*)\\x00PrintData\\s*len[\\S*\\s]*元");
                Matcher totalMatcher = totalPattern.matcher(msg);

                Pattern totalPattern1 = Pattern.compile("合计(\\d+)元");
                Matcher totalMatcher1 = totalPattern1.matcher(msg);
                if (totalMatcher1.find()) {
                    info[6] = totalMatcher1.group(1);
                } else if (totalMatcher.find()) {
                    info[6] = totalMatcher.group(1);
                }
                //查询spValue
                info[4] = TicketInfoExtract.format(msg, ticket);
                //找出票时间
                String time = GetTime.proccess(ticket, msg);
                info[7] = time;

            } else {

                //落地号格式[203399-636850-959631-40]新票机竞彩格式
                Pattern newJcPattern = Pattern.compile("[0-9]{6}\\-[0-9]{6}\\-[0-9]{6}\\-[0-9]{2}");
                Matcher newJcMatcher = newJcPattern.matcher(msg);
                if (newJcMatcher.find()) {
                    info[0] = newJcMatcher.group(0);

                    //密码
                    Pattern pwdPattern = Pattern.compile("[0-9]{6}\\-[0-9]{6}\\-[0-9]{6}\\-[0-9]{2}\\S*\\s*\\S*\\s*([0-9]{8})");
                    Matcher pwdMatcher = pwdPattern.matcher(msg);
                    int pwdIndex = 0;
                    if (pwdMatcher.find()) {
                        info[2] = pwdMatcher.group(1);
                    }
                    //找倍数
                    Pattern multiplePattern = Pattern.compile("倍数:([0-9]+)\\s+");
                    Matcher multipleMatcher = multiplePattern.matcher(msg);
                    multipleMatcher.find();
                    info[5] = multipleMatcher.group(1);


                    //找合计 6
                    Pattern totalPattern = Pattern.compile("合计:\\s*(\\d+)元");
                    Matcher totalMatcher = totalPattern.matcher(msg);
                    Pattern totalPattern1 = Pattern.compile("合计(\\d+)元");
                    Matcher totalMatcher1 = totalPattern1.matcher(msg);
                    if (totalMatcher.find()) {
                        info[6] = totalMatcher.group(1);
                    } else if (totalMatcher1.find()) {
                        info[6] = totalMatcher1.group(1);
                    }

                    //查询spValue
                    info[4] = TicketInfoExtract.format(msg, ticket);
                    //找出票时间
                    String time = GetTime.proccess(ticket, msg);
                    info[7] = time;

                } else {

                    //落地号格式[00007441566000040711]老票机格式
                    Pattern oldPattern = Pattern.compile("\\d{20}");
                    Matcher oldMatcher = oldPattern.matcher(msg);

                    if (oldMatcher.find()) {
                        String line = msg.substring(oldMatcher.start(), oldMatcher.start() + 42);

                        info[0] = line.split("\\s+")[0];
                        info[1] = line.split("\\s+")[1];
                        //查询spValue
                        info[4] = TicketInfoExtract.format(msg, ticket);

                        //找倍数
                        int beiShuBegin = 0;
                        int beiShuEnd = msg.indexOf("合计:");
                        if (Integer.parseInt(ticket.getGameCode() + "") >= 501) {
                            info[2] = line.split("\\s+")[2];
                            beiShuBegin = msg.indexOf("倍数:");
                            info[5] = msg.substring(beiShuBegin + 3, beiShuEnd).trim();
                        } else {
                            info[2] = line.split("\\s+")[2].replaceAll("P", "");
                            beiShuBegin = msg.indexOf("倍");
                            info[5] = msg.substring(beiShuBegin + 2, beiShuEnd).trim();
                        }

                        //找合计
                        String sumString = msg.substring(beiShuEnd);
                        int sumEnd = sumString.indexOf("元");
                        info[6] = sumString.substring(3, sumEnd).trim();

                        //找出票时间
                        String time = GetTime.proccess(ticket, msg);
                        info[7] = time;
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.info(TicketUtil.class, "抽取出票信息", "异常:" + LogUtil.getStackTrace(e));
        }
        LogUtil.info(TicketUtil.class, "抽取出票信息", "落地号=" + info[0] + "  校验码=" + info[1] + " 密码=" + info[2] + " 黑标=" + info[3] + " 赔率=" + info[4] + " 倍数=" + info[5] + " 金额=" + info[6] + " 出票时间=" + info[7] + "过关方式=" + info[8]);
        return info;
    }

    public static String getMannerInfo(String ticketInfo) {
        String manner = null;
        try {
            StringBuilder sb = new StringBuilder();
            if (StringUtils.isNotEmpty(ticketInfo)) {
                String data = "";
                String tmp = "";
                data = ticketInfo.replace("\u001BE", "").replace("P怂\u001B3", "").replace("\u001B3", "").replace("\u001B", "").replace("\u0001", "")
                        .replace("\u001E", "").replace("\u0002", "").replace("\u001D", "").replace("\u0010", "").replace("\u001D", "").replace("a", "").replace("\u001A", "")
                        .replace("d", "").replace("P", "").replace("怂", "").replace("!", "").replaceAll("M\\x00", "").replace("M", "")
                        .replace("#", "").replaceAll("\\x00", "").replaceAll("\\x00", "");
                String regs = "\\d+x\\d+";
                Pattern pattern = Pattern.compile(regs);
                Matcher matcher = pattern.matcher(data);
                if (matcher.find()) {
                    tmp = matcher.group(0);
                }
                if ("".equals(tmp)) {
                    regs = "场-(\\S*)关";
                    Pattern pattern1 = Pattern.compile(regs);
                    Matcher matcher1 = pattern1.matcher(data);
                    if (matcher1.find()) {
                        tmp = matcher1.group(1);
                    }
                    if (!StringUtils.isEmpty(tmp)) {
                        tmp = tmp + ",0";
                    }
                }
                if (tmp.contains(",")) {
                    String[] manners = tmp.split(",");
                    for (int i = 0; i < manners.length; i++) {
                        sb.append(JingCaiContants.JC_TICKEMODE.get("J" + manners[i] + "x1"));
                        if (i >= manners.length - 1) {
                            break;
                        }
                        sb.append(",");
                    }
                } else if (tmp.contains("x")) {
                    sb.append(JingCaiContants.JC_TICKEMODE.get("J" + tmp));
                } else {
                    sb.append(JingCaiContants.JC_TICKEMODE.get("J单关"));
                }
                manner = sb.toString();
            }
        } catch (Exception e) {
            LogUtil.info(TicketUtil.class, "抽取出票信息", "获取玩法出现异常" + LogUtil.getStackTrace(e));
        }
        return manner;
    }

    /**
     * 验校竞彩
     *
     * @param info
     * @param chipinTempDTO
     * @param manner
     * @return
     */
    public static boolean checkJC(String[] info, ChipinTempDTO chipinTempDTO, String manner) throws CheckTicketException {
        SystemExceptionService systemExceptionService = (SystemExceptionService) ServiceFactory.getService("systemExceptionService");
        try {
            String spValue = formatSpValue(info[4], (int) chipinTempDTO.getGameCode());//从票面上提取spValue
            String tptNumber = fromSpValueExtractVoteNumber((int) chipinTempDTO.getGameCode(), spValue);//从票面上提取的投注号码
            if (Integer.parseInt(chipinTempDTO.getMultiple()) != Integer.parseInt(info[5])) {
                systemExceptionService.insert("倍数打错!!!!!实际票面倍数:" + info[5], null);
                throw new CheckTicketException("倍数打错!!!!!");
            }
            if (!isManner(manner, chipinTempDTO.getManner())) {
                String msg = chipinTempDTO.getManner() + "(普通过关)";
                if (chipinTempDTO.getManner().contains(",0")) {
                    msg = chipinTempDTO.getManner() + "(自由过关)";
                }
                systemExceptionService.insert("玩法不相符!!!!!实际票面玩法:" + msg, null);
                throw new CheckTicketException("玩法不相符!!!!!");
            }
            if (chipinTempDTO.getBets() - Double.parseDouble(info[6]) != 0) {
                systemExceptionService.insert("金额不相符!!!!!实际票面金额:" + info[6], null);
                throw new CheckTicketException("金额不相符!!!!!");
            }
            if (!verificationNumber(chipinTempDTO.getChipinNums(), tptNumber)) {
                systemExceptionService.insert("号码不相符!!!!!实际票面号码:" + tptNumber, null);
                throw new CheckTicketException("号码不相符!!!!!");
            }
            if (!verificationSpvalue1(chipinTempDTO.getChipinNums(), spValue)) {
                systemExceptionService.insert("出票异常：赔率获取错误!!!!!", null);
                throw new CheckTicketException("出票异常：赔率获取错误!!!!!");
            }
            return true;
        } catch (Exception e) {
            if (e instanceof CheckTicketException) {
                throw new CheckTicketException(e.getMessage());
            } else {
                throw new SystemEroorException(e.getMessage());
            }
        }
    }

    /**
     * 验校数字彩
     *
     * @param chipinTempDTO
     * @return
     */
    public static boolean checkGP(ChipinTempDTO chipinTempDTO, String ticketInfo, PrintServiceDTO printServiceDTO) throws CheckTicketException {
        SystemExceptionService systemExceptionService = (SystemExceptionService) ServiceFactory.getService("systemExceptionService");
        try {
            Chipin chipin = null;
            if ("3".equals(printServiceDTO.getVersion())) {
                chipin = FormatConvertV3.stringConvert(ticketInfo, chipinTempDTO.getGameCode());
            } else {
                chipin = FormatConvertV2.stringConvert(ticketInfo, chipinTempDTO.getGameCode());
            }
            LogUtil.info(TicketUtil.class, "校验高频", "投注数据 倍数=" + chipinTempDTO.getMultiple() + " 金额=" + chipinTempDTO.getBets() + " 投注号码=" + chipinTempDTO.getChipinNums() + " 投注方式=" + chipinTempDTO.getManner() + " 期号=" + chipinTempDTO.getIssue());
            LogUtil.info(TicketUtil.class, "校验高频", "出票数据 倍数=" + chipin.getMultiple() + " 金额=" + chipin.getBets() + "  投注号码=" + chipin.getChipinNums() + " 投注方式=" + chipin.getManner() + " 期号=" + chipin.getIssue());
            if (Integer.parseInt(chipinTempDTO.getMultiple()) != chipin.getMultiple()) {
                systemExceptionService.insert("倍数打错!!!!!实际票面倍数:" + chipin.getMultiple(), null);
                throw new CheckTicketException("倍数打错!!!!!");
            }

            if (chipinTempDTO.getBets() - chipin.getBets() != 0) {
                systemExceptionService.insert("金额不相符!!!!!实际票面金额:" + chipin.getBets(), null);
                throw new CheckTicketException("金额不相符!!!!!");
            }

            if (!chipinTempDTO.getIssue().equals(chipin.getIssue())) {
                systemExceptionService.insert("期号错误!!!!!实际票面期号:" + chipin.getIssue(), null);
                throw new CheckTicketException("期号错误!!!!!");
            }

            if (!chipinTempDTO.getManner().equals(chipin.getManner().trim())) {
                systemExceptionService.insert("投注方法错误!!!!!实际票面投注方式:" + chipin.getManner(), null);
                throw new CheckTicketException("投注方法错误!!!!!");
            }
            if (!CheckCTUtil.execute(Integer.parseInt(chipinTempDTO.getGameCode() + ""), chipinTempDTO.getManner(), chipinTempDTO.getChipinNums(), chipin.getChipinNums())) {
                systemExceptionService.insert("号码不相符!!!!!实际票面号码:" + chipin.getChipinNums(), null);
                throw new CheckTicketException("号码不相符!!!!!");
            }
        } catch (Exception e) {
            if (e instanceof CheckTicketException) {
                throw new CheckTicketException(e.getMessage());
            } else {
                throw new SystemEroorException(e.getMessage());
            }
        }
        return true;
    }

    /**
     * 验校数字彩
     *
     * @param chipinTempDTO
     * @return
     */
    public static boolean checkCT(ChipinTempDTO chipinTempDTO, String ticketInfo, PrintServiceDTO printServiceDTO) throws CheckTicketException {
        SystemExceptionService systemExceptionService = (SystemExceptionService) ServiceFactory.getService("systemExceptionService");
        try {
            Chipin chipin = null;
            if ("3".equals(printServiceDTO.getVersion())) {
                chipin = FormatConvertV3.stringConvert(ticketInfo, chipinTempDTO.getGameCode());
            } else {
                chipin = FormatConvertV2.stringConvert(ticketInfo, chipinTempDTO.getGameCode());
            }
            if (chipinTempDTO.getGameCode() == 105 && chipinTempDTO.getManner().equals("23") ||
                    chipinTempDTO.getGameCode() == 105 && chipinTempDTO.getManner().equals("26") ||
                    chipinTempDTO.getGameCode() == 105 && chipinTempDTO.getManner().equals("42")) {
                chipinTempDTO.setChipinNums(chipinTempDTO.getChipinNums().replaceAll("//", "/"));
            }
            if (chipinTempDTO.getGameCode() == 105 && (chipinTempDTO.getManner().equals("13") || chipinTempDTO.getManner().equals("16"))) {
                chipinTempDTO.setManner(iszuXuan(chipinTempDTO.getChipinNums()));
            }
            LogUtil.info(TicketUtil.class, "校验数字彩", "投注数据 倍数=" + chipinTempDTO.getMultiple() + " 金额=" + chipinTempDTO.getBets() + " 投注号码=" + chipinTempDTO.getChipinNums() + " 投注方式=" + chipinTempDTO.getManner() + " 期号=" + chipinTempDTO.getIssue());
            LogUtil.info(TicketUtil.class, "校验数字彩", "出票数据 倍数=" + chipin.getMultiple() + " 金额=" + chipin.getBets() + "  投注号码=" + chipin.getChipinNums() + " 投注方式=" + chipin.getManner() + " 期号=" + chipin.getIssue());
            if (Integer.parseInt(chipinTempDTO.getMultiple()) != chipin.getMultiple()) {
                systemExceptionService.insert("倍数打错!!!!!实际票面倍数:" + chipin.getManner(), null);
                throw new CheckTicketException("倍数打错!!!!!");
            }

            if (chipinTempDTO.getBets() - chipin.getBets() != 0) {
                systemExceptionService.insert("金额不相符!!!!!实际票面金额:" + chipin.getBets(), null);
                throw new CheckTicketException("金额不相符!!!!!");
            }
            if (!chipinTempDTO.getIssue().equals(chipin.getIssue().trim())) {
                systemExceptionService.insert("期号错误!!!!!实际票面期号:" + chipin.getIssue(), null);
                throw new CheckTicketException("期号错误!!!!!");
            }
            if (!chipinTempDTO.getManner().equals(chipin.getManner().trim())) {
                systemExceptionService.insert("投注方法错误!!!!!实际票面投注方式:" + chipin.getIssue(), null);
                throw new CheckTicketException("投注方法错误!!!!!");
            }
            if (!CheckCTUtil.execute(Integer.parseInt(chipinTempDTO.getGameCode() + ""), chipinTempDTO.getManner(), chipinTempDTO.getChipinNums(), chipin.getChipinNums())) {
                systemExceptionService.insert("号码不相符!!!!!实际票面号码:" + chipin.getChipinNums(), null);
                throw new CheckTicketException("号码不相符!!!!!");
            }
        } catch (Exception e) {
            if (e instanceof CheckTicketException) {
                throw new CheckTicketException(e.getMessage());
            } else {
                throw new SystemEroorException(e.getMessage());
            }
        }
        return true;
    }

    /**
     * 获取北单 出票信息
     *
     * @param ticketInfo
     */
    public static String[] getBdPrintInfo(ChipinTempDTO ticket, String ticketInfo) throws SystemEroorException {
        String[] info = new String[10];
        try {
            //匹配玩法
            Pattern mannerReg = Pattern.compile("([0-9]+串[0-9]+)");
            Matcher mannerMatcher = mannerReg.matcher(ticketInfo);
            if (mannerMatcher.find()) {
                info[0] = mannerMatcher.group(1);
            } else {
                mannerReg = Pattern.compile("(单关)");
                mannerMatcher = mannerReg.matcher(ticketInfo);
                if (mannerMatcher.find()) {
                    info[0] = mannerMatcher.group(1);
                }
            }
            //匹配时间
            Pattern timeReg = Pattern.compile("时间([0-9]{4}-[0-9]{2}-[0-9]{2}\\s*[0-9]{2}:[0-9]{2}:[0-9]{2})");
            Matcher timeMatcher = timeReg.matcher(ticketInfo);
            if (timeMatcher.find()) {
                info[1] = timeMatcher.group(1);
            }
            //匹配期号
            Pattern issueReg = Pattern.compile("第([0-9]+)期");
            Matcher issueMatcher = issueReg.matcher(ticketInfo);
            if (issueMatcher.find()) {
                info[2] = issueMatcher.group(1);
            }
            //共多少期
            Pattern sumIssueReg = Pattern.compile("共\\s*([0-9]+)\\s*期");
            Matcher sumIssueMatcher = sumIssueReg.matcher(ticketInfo);
            if (sumIssueMatcher.find()) {
                info[3] = sumIssueMatcher.group(1);
            }
            //倍数
            Pattern multReg = Pattern.compile("\\s*([0-9]+)\\s*倍");
            Matcher mulMatcher = multReg.matcher(ticketInfo);
            if (mulMatcher.find()) {
                info[4] = mulMatcher.group(1);
            }
            //总金额
            Pattern moneyReg = Pattern.compile("\\s*([0-9]+)\\s*元");
            Matcher moneyMatcher = moneyReg.matcher(ticketInfo);
            if (moneyMatcher.find()) {
                info[5] = moneyMatcher.group(1);
            }
            //落地号
            Pattern ldhReg = Pattern.compile("([0-9]{6}-[0-9]{6}-[0-9]{6}-[0-9]{6})\\s*");
            Matcher ldhMatcher = ldhReg.matcher(ticketInfo);
            if (ldhMatcher.find()) {
                info[6] = ldhMatcher.group(1);
            }
            //密码
            Pattern passReg = Pattern.compile("\\s*([a-zA-Z0-9]{13})\\s*");
            Matcher passMatcher = passReg.matcher(ticketInfo);
            if (passMatcher.find()) {
                info[7] = passMatcher.group(1);
            }
            //游戏
            Pattern gameReg = Pattern.compile("\\s*\\[([\\u4e00-\\u9fa5]+)\\]\\s*");
            Matcher gameMatcher = gameReg.matcher(ticketInfo);
            if (gameMatcher.find()) {
                info[8] = gameMatcher.group(1);
            }

            //保存投注内容
            StringBuilder sb = new StringBuilder();
            //场次匹配
            Pattern cc = null;
            Matcher ccMatcher = null;
            //匹配投注内容
            if (ticket != null && StringUtils.isNotEmpty(ticket.getGameCode() + "")) {
                if ("301".equals(ticket.getGameCode() + "")) {
                    cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\s*((-\\s*)*([0-9]\\s*)*(-\\s*)*)([\\n\\[])");
                } else if ("302".equals(ticket.getGameCode() + "")) {
                    cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\s*((上单\\s*)*(——\\s*)*(上双\\s*)*(——\\s*)*(下单\\s*)*(——\\s*)*(下双\\s*)*)");
                } else if ("303".equals(ticket.getGameCode() + "")) {
                    cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\s*(([0-7]\\+*\\s*)*(-\\s*)*([0-7]\\+*\\s*)*(-\\s*)*([0-7]\\+*\\s*)*(-\\s*)*([0-7]\\+*\\s*)*(-\\s*)*([0-7]\\+*\\s*)*)\\n");
                } else if ("304".equals(ticket.getGameCode() + "")) {
                    cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\)\\s*\\n((([0-9]\\s*:[0-9]\\s*)+|(-\\s*)+|(\\n)+)+)");
                } else if ("305".equals(ticket.getGameCode() + "")) {
                    //cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\)\\s*\\n(([0-9]-[0-9]\\s*)*(-\\s*)*([0-9]-[0-9]\\s*)*(-\\s*)*([0-9]-[0-9]\\s*)*(-\\s*)*([0-9]-[0-9]\\s*)*(-\\s*)*([0-9]-[0-9]\\s*)*)");
                    cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\)\\s*\\n(((-\\s*){3,}|([0-9]-[0-9]\\s*)|\\n+)+)");
                } else if ("306".equals(ticket.getGameCode() + "")) {
                    //cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\s*((胜\\s*)*(负\\s*)*(-\\s*)*(胜\\s*)*(负\\s*)*)([\\[\\n])");
                    cc = Pattern.compile("([0-9]+)\\.[a-zA-Z0-9\\u4e00-\\u9fa5]+\\s*((((胜\\s*)*|(负\\s*)*)*|(-\\s*)*)+)[\\n\\[]");
                } else if ("307".equals(ticket.getGameCode() + "")) {

                }
                //循环匹配从票机返回的场次信息
                ccMatcher = cc.matcher(ticketInfo);
                while (ccMatcher.find()) {
                    String changci = ccMatcher.group(1);//场次
                    String playWays = ccMatcher.group(2);//玩法
                    if (StringUtils.isNotEmpty(changci) && StringUtils.isNotEmpty(playWays) && validePlayWays(playWays)) {
                        String resultPlays = playWaysConvert(ticket.getGameCode() + "", playWays);
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

            LogUtil.info(TicketUtil.class, "获取北单出票信息", "玩法=" + info[0] + "  出票时间=" + info[1] + " 期号=" + info[2] + " 共多少期=" + info[3] + " 倍数=" + info[4] + " 总金额=" + info[5] + " 落地号=" + info[6] + " 密码=" + info[7] + " 游戏=" + info[8] + " 场次=" + info[9]);
        } catch (Exception e) {
            throw new SystemEroorException(e);
        }
        return info;
    }

    /**
     * 验校北单
     *
     * @param info
     * @param chipinTempDTO
     * @return
     */
    public static boolean checkBd(String[] info, ChipinTempDTO chipinTempDTO) throws CheckTicketException, SQLException {
        SystemExceptionService systemExceptionService = (SystemExceptionService) ServiceFactory.getService("systemExceptionService");
        if (!checkVoteContent(info, chipinTempDTO)) {
            systemExceptionService.insert("号码不相符!!!!!实际票面号码:" + info[9], null);
            throw new CheckTicketException("号码不相符!!!!!");
        }
        if (!info[2].equals(chipinTempDTO.getIssue())) {
            systemExceptionService.insert("期号不相符!!!!!实际票面期号:" + info[2], null);
            throw new CheckTicketException("期号不相符!!!!");
        }
        if (!info[4].equals(chipinTempDTO.getMultiple())) {
            systemExceptionService.insert("倍数打错!!!!!实际票面倍数:" + info[4], null);
            throw new CheckTicketException("倍数打错!!!!!");
        }
        if (chipinTempDTO.getBets() - Double.parseDouble(info[5]) != 0) {
            systemExceptionService.insert("金额不相符!!!!!实际票面金额:" + info[5], null);
            throw new CheckTicketException("金额不相符!!!!!");
        }
        if (!info[0].equals(GameContent.bjdc_manner_to_name.get(chipinTempDTO.getManner()))) {
            systemExceptionService.insert("玩法不相符!!!!!票机所出票玩法：" + info[0] + "实际票面玩法:" + chipinTempDTO.getManner(), null);
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

    private static String iszuXuan(String msg) {
        String hao = msg.split(",")[0];
        String[] msgs = hao.split("/");
        HashSet hashSet = new HashSet();
        for (int i = 0; i < msgs.length; i++) {
            hashSet.add(msgs[i]);
        }
        if (hashSet.size() == 2) {
            return "13";
        } else {
            return "16";
        }
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
            LogUtil.info(TicketUtil.class, "校验", "游戏307还没有增加 下半场比分玩法...");
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

    private static boolean isManner(String ticketManners, String chipinManners) {
        String ticketManner = "";
        String chipinManner = "";
        if (ticketManners.contains(",0")) {
            if (!chipinManners.contains(",0")) {//数据库不存在有 ,0
                return false;
            }
            ticketManners = ticketManners.substring(0, ticketManners.indexOf(",0"));
            chipinManners = chipinManners.substring(0, chipinManners.indexOf(",0"));
            if (ticketManners.contains(",")) {
                if (!chipinManners.contains(",")) { //两个数据不一样
                    return false;
                }
                String[] strs = ticketManners.split(",");
                ticketManner = sortString(strs);

                String[] strs1 = chipinManners.split(",");
                chipinManner = sortString(strs1);
            } else {
                chipinManner = chipinManners;
                ticketManner = ticketManners;
            }

        } else {
            chipinManner = chipinManners;
            ticketManner = ticketManners;
        }
        if (!ticketManner.equals(chipinManner)) {
            return false;
        }
        return true;
    }

    /**
     * 比较两个号码是不是相等
     *
     * @param voteNumber1
     * @param voteNumber2
     */
    public static boolean verificationNumber(String voteNumber1, String voteNumber2) {
        Map<String, List> map = getChipinNums(voteNumber1);

        String[] msg = voteNumber2.split("//");
        for (int i = 0; i < msg.length; i++) {
            String[] content = msg[i].split(",");

            List list = map.get(content[0]);
            if (null != list) {
                String[] voteNumber = content[1].split("/");
                for (int j = 0; j < voteNumber.length; j++) {
                    if (!list.contains(voteNumber[j])) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }

        return true;
    }

    private static Map<String, List> getChipinNums(String voteNumber1) {
        Map<String, List> map = new HashMap<String, List>();
        try {

            String[] msg = voteNumber1.split("//");
            for (int i = 0; i < msg.length; i++) {
                String[] content = msg[i].split(",");
                List list = new ArrayList();

                String[] voteNumber = content[1].split("/");
                for (int j = 0; j < voteNumber.length; j++) {
                    list.add(voteNumber[j]);
                }
                map.put(content[0], list);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private static String sortString(String[] strs) {
        int temp = 0;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strs.length - 1; i++) {
            for (int j = 0; j < strs.length - 1 - i; j++) {
                if (Integer.parseInt(strs[j]) > Integer.parseInt(strs[j + 1])) {
                    temp = Integer.parseInt(strs[j]);
                    strs[j] = strs[j + 1];
                    strs[j + 1] = String.valueOf(temp);
                }
            }
        }
        for (int i = 0; i < strs.length; i++) {
            sb.append(strs[i]);
            if (i >= strs.length - 1) {
                break;
            }
            sb.append(",");
        }
        return sb.toString();
    }

    /**
     * 验证每场对阵是否都取到SP值
     *
     * @param spValue
     * @return
     */
    private static boolean verificationSpvalue1(String sourceVoteNumber, String spValue) {
        boolean result = true;
        String[] msg = sourceVoteNumber.split("//");
        for (int i = 0; i < msg.length; i++) {
            String[] content = msg[i].split(",");
            String[] voteNumber = content[1].split("/");
            for (int j = 0; j < voteNumber.length; j++) {
                if (!verificationSpvalue2(content[0], voteNumber[j], spValue)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 验证每场对阵是否都取到SP值
     *
     * @param spValue
     * @return
     */
    private static boolean verificationSpvalue2(String changci, String sourceconent, String spValue) {
        boolean result = false;
        String[] msg = spValue.split("//");
        for (int i = 0; i < msg.length; i++) {
            String[] content = msg[i].split(",");
            if (changci.equals(content[0])) {
                Pattern p = Pattern.compile(sourceconent + "\\S*" + ":" + "\\d");
                Matcher m = p.matcher(content[1]);

                if (m.find()) {
                    result = true;
                }

                break;
            }

        }

        return result;

    }

    /**
     * 特殊处理篮球让分和大小分
     *
     * @param spValue
     * @param gameCode
     * @return
     */
    public static String formatSpValue(String spValue, int gameCode) {//周四001,3:2.100//周四002,3:2.150/1:3.050//周四003,0:3.700//周四004,1:3.350
        StringBuilder result = new StringBuilder();//以前格式： 周三301,1*(主-2.5)*(主-2.5)//周三302,0*(主-2.5)*(主-2.5)

        //现在格式： 赔率=周四301,1(主-9.5):1.700//周四302,0(主+4.5):1.700 倍数=1 金额=2 出票时间=20141023095030
        //需要得到的格式：周五301,1(-4.5):1.660//周六313,0(-2.5):1.750
        if (gameCode == 506 || gameCode == 508) {//
            String[] spAry = spValue.split("//");
            for (int i = 0; i < spAry.length; i++) {
                String[] chipinNums = spAry[i].split(",");
                String no = chipinNums[0];
                result.append(no).append(",");//对阵编号
                if (chipinNums.length > 1) {
                    String[] msgAry = chipinNums[1].split("/");
                    for (int j = 0; j < msgAry.length; j++) {
                        result.append(msgAry[j].replaceAll("主", ""));
                        if (j != msgAry.length - 1) {
                            result.append("/");
                        }
                    }
                }
                if (i != spAry.length - 1) {
                    result.append("//");
                }
            }
        } else if (gameCode == 501 || gameCode == 502 || gameCode == 503 || gameCode == 504 || gameCode == 505 || gameCode == 507 || gameCode == 511) {//周三003,310
            String[] spAry = spValue.split("//");
            for (int i = 0; i < spAry.length; i++) {
                String[] chipinNums = spAry[i].split(",");
                String no = chipinNums[0];
                result.append(no).append(",");
                if (chipinNums.length > 1) {
                    String[] msgAry = chipinNums[1].split("/");
                    for (int j = 0; j < msgAry.length; j++) {
                        result.append(msgAry[j]);
                        if (j != msgAry.length - 1) {
                            result.append("/");
                        }
                    }
                }
                if (i != spAry.length - 1) {
                    result.append("//");
                }
            }
        } else if (gameCode == 509) {
            String[] spAry = spValue.split("//");
            for (int i = 0; i < spAry.length; i++) {
                String[] chipinNums = spAry[i].split(",");
                String no = chipinNums[0];
                result.append(no).append(",");
                if (chipinNums.length > 1) {
                    String[] msgAry = chipinNums[1].split("/");
                    for (int j = 0; j < msgAry.length; j++) {
                        result.append(msgAry[j]);
                        if (j != msgAry.length - 1) {
                            result.append("/");
                        }
                    }
                }
                if (i != spAry.length - 1) {
                    result.append("//");
                }
            }
        } else if (gameCode == 510) {
            String[] spAry = spValue.split("//");
            for (int i = 0; i < spAry.length; i++) {
                String[] chipinNums = spAry[i].split(",");
                String no = chipinNums[0];
                result.append(no).append(",");
                if (chipinNums.length > 1) {
                    String[] msgAry = chipinNums[1].split("/");
                    for (int j = 0; j < msgAry.length; j++) {
                        result.append(msgAry[j]);
                        if (j != msgAry.length - 1) {
                            result.append("/");
                        }
                    }
                }
                if (i != spAry.length - 1) {
                    result.append("//");
                }
            }
        } else if (gameCode == 512 || gameCode == 513) {
            result.append(spValue);
        }
        return result.toString();
    }

    /**
     * 从spValue获取投注号码
     *
     * @param gameCode
     * @param spValue
     * @return
     */
    public static String fromSpValueExtractVoteNumber(int gameCode, String spValue) {
        StringBuilder strBuf = new StringBuilder();

        String[] msg = spValue.split("//");
        for (int i = 0; i < msg.length; i++) {
            String[] content = msg[i].split(",");
            strBuf.append(content[0]).append(",");

            String[] voteNumber = content[1].split("/");
            for (int j = 0; j < voteNumber.length; j++) {
                String[] voteResult = voteNumber[j].split("\\:");
                switch (gameCode) {
                    case 501:
                    case 503:
                    case 504:
                    case 505:
                    case 507:
                    case 511:
                        strBuf.append(voteResult[0]);
                        break;
                    case 502://周四001,4:3:80.00/1:0:11.00/2:0:20.00/2:1:11.00/3:0:40.00//周四002,1:0:5.500
                        strBuf.append(voteResult[0]).append(":").append(voteResult[1]);
                        break;
                    case 506://周五301,0(+6.5):1.700/1(+6.5):1.700//周五302,0(+4.5):1.600//周五303,1(-5.5):1.700//周五304,1(-7.5):1.700
                        strBuf.append(voteResult[0].substring(0, 1));
                        break;
                    case 508://周五301,1(154.5):1.700/2(154.5):1.700//周五302,1(151.5):1.700//周五303,2(153.5):1.700
                        strBuf.append(voteResult[0].substring(0, 1));
                        break;
                    case 509://周四004,3_3:2.700//周四005,1B:3.700//周四006,3:2.620/1:2.930//周四007,3:1:22.00
                        if (voteResult.length > 2) {
                            strBuf.append(voteResult[0]).append(":").append(voteResult[1]);
                        } else {
                            strBuf.append(voteResult[0]);
                        }
                        break;
                    //周三301,1B//周三302,1D//周三303,0/1
                    case 510://周三301,1B(150.5):1.700//周三302,1D(150.5):1.140//周三303,0(主-7.50)(150.5):1.700/1(主-7.50)(150.5):1.700
                        strBuf.append(voteResult[0].substring(0, -1 == voteResult[0].indexOf("(") ? voteResult[0].length() : voteResult[0].indexOf("(")));
                        break;
                    case 512://01,01:2.650/02:4.500/03:4.000
                    case 513:
                        strBuf.append(voteResult[0]);
                        break;
                }

                if (j != voteNumber.length - 1) {
                    strBuf.append("/");
                }
            }

            if (i != msg.length - 1) {
                strBuf.append("//");
            }
        }

        return strBuf.toString();
    }
}
