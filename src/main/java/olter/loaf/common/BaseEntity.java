package olter.loaf.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loaf_generator")
    @SequenceGenerator(
        name = "loaf_generator",
        sequenceName = "loaf_id_seq",
        initialValue = 1000000,
        allocationSize = 1)
    private Long id;
}
