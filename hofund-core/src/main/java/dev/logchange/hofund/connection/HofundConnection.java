package dev.logchange.hofund.connection;

import dev.logchange.hofund.StringUtils;
import dev.logchange.hofund.info.HofundInfoProvider;
import io.micrometer.core.instrument.Tag;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class HofundConnection {

    /**
     * Name of the resource that application connects to f.e. Cart-db, fcm, products-app
     */
    private final String target;
    private final String url;
    private final Type type;
    private final AtomicReference<StatusFunction> fun;
    private final String description;
    private String icon;

    public HofundConnection(String target, String url, Type type, AtomicReference<StatusFunction> fun, String description) {
        this.target = target;
        this.url = url;
        this.type = type;
        this.fun = fun;
        this.description = description;
    }

    public String toTargetTag() {
        return type == Type.DATABASE ? target + "_" + type : target;
    }

    public String getDescription() {
        return StringUtils.emptyIfNull(description);
    }

    public String getEdgeId(HofundInfoProvider infoProvider) {
        if (StringUtils.isEmpty(getDescription())) {
            return infoProvider.getApplicationName() + "-" + getTarget() + "_" + getType();
        }

        return infoProvider.getApplicationName() + "-" + getTarget() + "_" + getType() + "_" + getDescription().toLowerCase();
    }

    /**
     * Available icons:
     * <a href="https://developers.grafana.com/ui/latest/index.html?path=/story/docs-overview-icon--icons-overview">Grafana BuiltIn Icons</a>
     */
    public String getIcon() {
        if (StringUtils.isEmpty(icon)) {
            switch (type) {
                case DATABASE:
                    return "database";
                case FTP:
                    return "file-alt";
                case HTTP:
                    return "share-alt";
            }
        }

        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Tag> getTags(HofundInfoProvider infoProvider) {
        List<Tag> tags = new LinkedList<>();
        tags.add(Tag.of("id", getEdgeId(infoProvider)));
        tags.add(Tag.of("source", infoProvider.getApplicationName()));
        tags.add(Tag.of("target", toTargetTag()));
        tags.add(Tag.of("type", getType().toString()));
        return tags;
    }

    public String getTarget() {
        return target;
    }

    public String getUrl() {
        return url;
    }

    public Type getType() {
        return type;
    }

    public AtomicReference<StatusFunction> getFun() {
        return fun;
    }


}
