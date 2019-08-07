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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * <p>
 * 类描述：
 * </p>
 *
 * @author liujiayi
 * @since 2019/4/24 9:36
 * @version：1.0
 */
public class java8 {

    public static void main(String[] args) {
        List<Integer> strs = Arrays.asList(-1, 2, 3, 4, 5);
        Integer min = strs.stream().min(Comparator.comparing(k->k)).get();
        System.out.println(min);
    }
}
