CREATE TABLE notifications (
                               id BIGINT NOT NULL AUTO_INCREMENT,

                               recipient_id BIGINT NOT NULL,

                               actor_id BIGINT NULL,

                               type VARCHAR(50) NOT NULL,

                               title VARCHAR(150) NOT NULL,

                               message VARCHAR(500) NOT NULL,

                               entity_type VARCHAR(50) NULL,

                               entity_id BIGINT NULL,

                               is_read BOOLEAN NOT NULL DEFAULT FALSE,

                               created_at DATETIME NOT NULL,

                               PRIMARY KEY (id),

                               CONSTRAINT fk_notifications_recipient
                                   FOREIGN KEY (recipient_id)
                                       REFERENCES users(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_notifications_actor
                                   FOREIGN KEY (actor_id)
                                       REFERENCES users(id)
                                       ON DELETE SET NULL
);

CREATE INDEX idx_notifications_recipient
    ON notifications(recipient_id);

CREATE INDEX idx_notifications_recipient_read
    ON notifications(recipient_id, is_read);

CREATE INDEX idx_notifications_created_at
    ON notifications(created_at);