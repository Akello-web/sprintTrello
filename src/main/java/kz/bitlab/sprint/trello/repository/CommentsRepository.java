package kz.bitlab.sprint.trello.repository;

import kz.bitlab.sprint.trello.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
