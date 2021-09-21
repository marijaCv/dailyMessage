package de.pickert.module.dailymessage.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.data.mongodb.core.mapping.Document;

import de.pickert.module.core.domain.AbstractAuditingEntity;
import de.pickert.module.core.domain.Textstore;

@Document(collection = "daily_message")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DailyMessage extends AbstractAuditingEntity {

    private Textstore text;
    private Textstore title;
    private Integer index;
    private Integer dayToShow;
    private boolean show;
    private Boolean deleted;

    public DailyMessage() {
    }

    public DailyMessage(Textstore text, Integer dayToShow, boolean show) {
        this.text = text;
    }

    public void setTitle(Textstore title) {
        this.title = title;
    }

    public Textstore getTitle() {
        return title;
    }

    public void setText(Textstore text) {
        this.text = text;
    }

    public Textstore getText() {
        return text;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public Integer getDayToShow() {
        return dayToShow;
    }

    public void setDayToShow(int dayToShow) {
        this.dayToShow = dayToShow;
    }

}