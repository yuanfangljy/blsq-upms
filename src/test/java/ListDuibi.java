/**
 * Copyright (c) 2019-2030, 云宝网络科技有限公司 All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p>
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the 云宝网络科技有限公司 developer nor the names of its
 * 注意：本内容仅限于云宝网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 * Author: liujiayi
 */

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 类描述：
 * </p>
 *
 * @author liujiayi
 * @since 2019/4/19 9:07
 * @version：1.0
 */
public class ListDuibi {

    public static void main(String[] args) {
        List<UserOne> userOneList=new ArrayList<>();
        UserOne userOne =new UserOne();
        userOne.setUuid("11111");
        userOne.setUserName("liujiayi");
        userOne.setUuid("12345");
        userOne.setUserName("liujiay1i");
        userOneList.add(userOne);
        List<UserTwo> userTwoList=new ArrayList<>();
        UserTwo userTwo=new UserTwo();
        userTwo.setUuid("11111");
        userTwo.setSex("liujiayi");
        userTwo.setUuid("12345");
        userTwo.setSex("liujiay1i");
        userTwoList.add(userTwo);

        System.out.println(userOneList.toString());
    }
}
