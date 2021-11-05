package com.andyron.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author Andy Ron
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
// 在企业开发中，所有POJO一般度需要可序列化
public class User  implements Serializable {

    private String name;
    private int age;
}
