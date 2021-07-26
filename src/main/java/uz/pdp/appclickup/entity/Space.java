package uz.pdp.appclickup.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.appclickup.entity.template.AbsLongEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Space extends AbsLongEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String initialLetter;

    @ManyToOne(fetch = FetchType.LAZY)
    private Icon icon;

    @OneToOne
    private Attachment avatar;

    @Column(nullable = false)
    private String accessType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @PrePersist
    @PreUpdate
    public void setInitialLetterMyMethod() {
        this.initialLetter = name.substring(0,1);
    }

    public Space(String name, String color, Icon icon, Attachment avatar, String accessType, Workspace workspace, User owner) {
        this.name = name;
        this.color = color;
        this.icon = icon;
        this.avatar = avatar;
        this.accessType = accessType;
        this.workspace = workspace;
        this.owner = owner;
    }
}
