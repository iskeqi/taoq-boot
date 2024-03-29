package tech.taoq.common.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * AuthBO
 *
 * @author keqi
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AuthBO<T> {

    private String key;

    private T data;
}
