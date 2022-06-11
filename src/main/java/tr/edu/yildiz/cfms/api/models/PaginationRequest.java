package tr.edu.yildiz.cfms.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class PaginationRequest {
    @Getter
    @Setter
    int offset = 0;

    @Getter
    @Setter
    int limit = 500;
}
