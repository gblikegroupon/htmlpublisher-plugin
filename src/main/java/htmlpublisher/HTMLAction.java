package htmlpublisher;

import hudson.model.AbstractItem;
import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;
import hudson.model.Run;

import java.io.File;

public class HTMLAction extends BaseHTMLAction implements ProminentProjectAction {
    private HtmlPublisherTarget htmlPublisherTarget;
    private final AbstractItem project;

    public HTMLAction(HtmlPublisherTarget htmlPublisherTarget, AbstractItem project, HtmlPublisherTarget actualHtmlPublisherTarget) {
        super(actualHtmlPublisherTarget);
        this.htmlPublisherTarget = htmlPublisherTarget;
        this.project = project;
    }

    @Override
    protected File dir() {
        if (this.project instanceof AbstractProject) {
            AbstractProject abstractProject = (AbstractProject) this.project;

            Run run = abstractProject.getLastSuccessfulBuild();
            if (run != null) {
                File javadocDir = htmlPublisherTarget.getBuildArchiveDir(run);

                if (javadocDir.exists()) {
                    return javadocDir;
                }
            }
        }

        return htmlPublisherTarget.getProjectArchiveDir(this.project);
    }

    @Override
    protected String getTitle() {
        return this.project.getDisplayName() + " html2";
    }
}
