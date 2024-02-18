package olter.loaf.lobby.messages.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "messages")
public class MessageEntity extends BaseEntity {
    private Long lobbyId;
    private Long senderId;

    @CreationTimestamp
    private Instant timestamp;

    @Column(columnDefinition = "TEXT")
    private String message;
}
