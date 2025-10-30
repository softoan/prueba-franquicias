package  com.pbfranquicias.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "franchises")
public class Franchise {
    @Id
    private String id;

    @Field("name")
    private String name;

    private List<Branch> branches = new ArrayList<>();
}
