package com.my_solution.sorftaria_test_task.encoders;

import com.my_solution.sorftaria_test_task.configurations.AlgorithmConfiguration;
import com.my_solution.sorftaria_test_task.encoders.exceptions.AlgorithmNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * assuming that source code of the HTML files
 * is stored in basic Java String objects
 */

@Component
public class HTMLSourceToHashEncoder implements IEncoder<String> {

    private final AlgorithmConfiguration configuration;
    private final Charset charset;

    @Autowired
    public HTMLSourceToHashEncoder(AlgorithmConfiguration algorithmConfiguration, Charset charset) {
        configuration = algorithmConfiguration;
        this.charset = charset;
    }

    @Override
    public byte[] encode(String source) {
        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance(configuration.getAlgorithmName());
            return messageDigest.digest(source.getBytes(charset));
        }
        catch (NoSuchAlgorithmException algorithmException) {
            throw new AlgorithmNotFoundException("Unable to recognize the algorithm with name: " +
                    configuration.getAlgorithmName(), algorithmException);
        }
    }
}
