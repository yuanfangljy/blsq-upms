package com.blsq.admin.common.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 类描述： aes 解密
 * </p>
 *
 * @author liujiayi
 * @since 2019/4/4 9:56
 * @version：1.0
 */
public class DecryptAES {

    private static final String PASSWORD = "password";
    private static final String KEY_ALGORITHM = "AES";

    @Getter
    @Value("${security.encode.key:cooeccooeccooece}")
    private String encodeKey;

    @SneakyThrows
    public  static String decryptAES(String data, String pass) {
        AES aes = new AES(Mode.CBC, Padding.NoPadding,
                new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM),
                new IvParameterSpec(pass.getBytes()));
        byte[] result = aes.decrypt(Base64.decode(data.getBytes(StandardCharsets.UTF_8)));
        return new String(result, StandardCharsets.UTF_8);
    }
}
