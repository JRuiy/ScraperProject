package scraper.project.testScraper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import scraper.project.core.models.Exportable;

@Entity
@Getter
@Setter
public class SearchModel extends Exportable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String fullName;
    @Column
    private int asdas;
}