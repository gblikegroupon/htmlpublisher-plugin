package htmlpublisher;

import hudson.FilePath;
import hudson.model.Action;
import hudson.model.DirectoryBrowserSupport;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

abstract class BaseHTMLAction implements Action {
    private HtmlPublisherTarget actualHtmlPublisherTarget;

    public BaseHTMLAction(HtmlPublisherTarget actualHtmlPublisherTarget) {
        this.actualHtmlPublisherTarget = actualHtmlPublisherTarget;
    }

    public String getUrlName() {
        return actualHtmlPublisherTarget.getSanitizedName();
    }

    public String getDisplayName() {
        String action = getHtmlPublisherTarget().getWrapperName();
        return dir().exists() ? action : null;
    }

    public String getIconFileName() {
        return dir().exists() ? "graph.gif" : null;
    }

    protected HtmlPublisherTarget getHtmlPublisherTarget() {
        return actualHtmlPublisherTarget;
    }

    /**
     * Serves HTML reports.
     */
    public void doDynamic(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        DirectoryBrowserSupport dbs = new DirectoryBrowserSupport(this, new FilePath(this.dir()), this.getTitle(), "graph.gif", false);
        dbs.setIndexFileName(getHtmlPublisherTarget().getWrapperName()); // Hudson >= 1.312
        dbs.generateResponse(req, rsp, this);
    }

    protected abstract String getTitle();

    protected abstract File dir();
}
