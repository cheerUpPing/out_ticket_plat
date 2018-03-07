package com.harmony.service;

/**
 * 2017/9/13 15:32.
 * <p>
 * Email: cheerUpPing@163.com
 */
public interface BaseService<T, ID> {

    void add(T t);

    void delete(ID id);

    void update(T t);

    T select(ID id);


}
