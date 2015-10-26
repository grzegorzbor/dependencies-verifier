package dependencies;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

@Mojo(name = "verify", requiresDependencyResolution = ResolutionScope.RUNTIME, defaultPhase = LifecyclePhase.VERIFY)
public class VerifierPlugin extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException {
        String classes = project.getBuild().getOutputDirectory();
        File config = new File(classes, "dependencies.properties");
        if (!config.exists()) {
            throw new MojoExecutionException("Configuration file not found on the classpath: dependencies.properties");
        }
        Properties deps;
        try {
            InputStream properties = new FileInputStream(config);
            deps = new Properties();
            deps.load(properties);
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
        try {
            new SimpleVerifier(new HashMap(deps)).verify(classes);
            getLog().info("Package dependencies analyzed, no violations found");
        } catch (DependencyVerificationException e) {
            throw new MojoFailureException(e.getMessage(), e);
        }
    }
}