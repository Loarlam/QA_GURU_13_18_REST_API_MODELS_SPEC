package qa.guru.models.lombok;

import lombok.Data;

@Data
public class CreateUserResponseLombokModel {
    private String name,
            job,
            id,
            createdAt;
}
