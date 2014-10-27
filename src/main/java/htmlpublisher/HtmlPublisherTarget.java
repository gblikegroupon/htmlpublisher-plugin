package htmlpublisher;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.AbstractItem;
import hudson.model.AbstractProject;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Action;
import hudson.model.Run;
import hudson.model.Descriptor;
import hudson.Extension;


import java.io.File;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * A representation of an HTML directory to archive and publish.
 *
 * @author Mike Rooney
 *
 */
public class HtmlPublisherTarget extends AbstractDescribableImpl<HtmlPublisherTarget> {
    /**
     * The name of the report to display for the build/project, such as "Code Coverage"
     */
    private final String reportName;

    /**
     * The path to the HTML report directory relative to the workspace.
     */
    private final String reportDir;

    /**
     * The file[s] to provide links inside the report directory.
     */
    private final String reportFiles;

    /**
     * If true, archive reports for all successful builds, otherwise only the most recent.
     */
    private final boolean keepAll;

    /**
     * If true, will allow report to be missing and build will not fail on missing report.
     */
    private final boolean allowMissing;

    /**
     * The name of the file which will be used as the wrapper index.
     */
    private final String wrapperName = "htmlpublisher-wrapper.html";

    @DataBoundConstructor
    public HtmlPublisherTarget(String reportName, String reportDir, String reportFiles, boolean keepAll, boolean allowMissing) {
        this.reportName = reportName;
        this.reportDir = reportDir;
        this.reportFiles = reportFiles;
        this.keepAll = keepAll;
        this.allowMissing = allowMissing;
    }

    public String getReportName() {
        return this.reportName;
    }

    public String getReportDir() {
        return this.reportDir;
    }

    public String getReportFiles() {
        return this.reportFiles;
    }

    public boolean getKeepAll() {
        return this.keepAll;
    }

    public boolean getAllowMissing() {
           return this.allowMissing;
    }

    public String getSanitizedName() {
        String safeName = this.reportName;
        safeName = safeName.replace(" ", "_");
        return safeName;
    }

    public String getWrapperName() {
        return this.wrapperName;
    }

    public FilePath getArchiveTarget(AbstractBuild build) {
        return new FilePath(this.keepAll ? getBuildArchiveDir(build) : getProjectArchiveDir(build.getProject()));
    }

    /**
     * Gets the directory where the HTML report is stored for the given project.
     */
    protected File getProjectArchiveDir(AbstractItem project) {
        return new File(new File(project.getRootDir(), "htmlreports"), this.getSanitizedName());
    }
    /**
     * Gets the directory where the HTML report is stored for the given build.
     */
    protected File getBuildArchiveDir(Run run) {
        return new File(new File(run.getRootDir(), "htmlreports"), this.getSanitizedName());
    }

    public void handleAction(AbstractBuild<?, ?> build) {
        // Add build action, if coverage is recorded for each build
        if (this.keepAll) {
            build.addAction(new HTMLBuildAction(build, this));
        }
    }

    public Action getProjectAction(AbstractProject project) {
        return new HTMLAction(this, project, this);
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<HtmlPublisherTarget> {
        public String getDisplayName() { return ""; }
    }
}