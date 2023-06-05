package kz.bitlab.sprint.trello.repository;

import kz.bitlab.sprint.trello.model.Folders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folders, Long> {
}
