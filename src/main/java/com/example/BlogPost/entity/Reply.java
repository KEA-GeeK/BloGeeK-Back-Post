package com.example.BlogPost.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity @Table(name = "reply")
public class Reply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reply_id;

    private Long author_id;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false) @CreationTimestamp
    private LocalDateTime create_at;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id", referencedColumnName = "comment_id")
    Comment comment;

    @Column(nullable = false) @UpdateTimestamp
    private LocalDateTime last_modified;

    public Reply() {
    }
}
