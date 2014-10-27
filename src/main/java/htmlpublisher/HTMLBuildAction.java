package htmlpublisher;

import hudson.model.AbstractBuild;

import java.io.File;

class HTMLBuildAction extends BaseHTMLAction {
    private final AbstractBuild<?, ?> build;

    public HTMLBuildAction(AbstractBuild<?, ?> build, HtmlPublisherTarget actualHtmlPublisherTarget) {
        super(actualHtmlPublisherTarget);
        this.build = build;
    }

    public final AbstractBuild<?,?> getOwner() {
        return build;
    }

    @Override
    protected String getTitle() {
        return this.build.getDisplayName() + " html3";
    }

    @Override
    protected File dir() {
        return getHtmlPublisherTarget().getBuildArchiveDir(this.build);
    }
}
