package project.Challenge_6BinarFood.dto.response.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//generic class
public class WebResponse<T> {
    private int countRecord;
    private T data;
    private String messages;
}
