package  com.pbfranquicias.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch {
    private String id;
    private String name;
    private List<Product> products = new ArrayList<>();
}
