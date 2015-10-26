package dependencies;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import jdepend.framework.PackageFilter;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SimpleVerifier {

     Map<String,Set<String>> allowedDependencies = new HashMap<String, Set<String>>();

    /**
     * @param allowedDependenciesMap key: package name - "dependency from";
     *                               value: comma-separate list of packages, allowed dependencies of the package
     */
    public SimpleVerifier(Map<String,String> allowedDependenciesMap) {
        for (Map.Entry<String, String> entry : allowedDependenciesMap.entrySet()) {
            this.allowedDependencies.put(entry.getKey(), new HashSet<>(Arrays.asList(entry.getValue().split(","))));
        }
    }

    /**
     * @param directoryWithClasses a path to the directory containing .class files to be analyzed
     */
    public void verify(String directoryWithClasses) {
        if (!new File(directoryWithClasses).exists()) {
            throw new IllegalArgumentException("Directory " + directoryWithClasses + " does not exist");
        }
        PackageFilter packageFilter = new PackageFilter();
        packageFilter.addPackage("java.*");
        JDepend jDepend = new JDepend(packageFilter);
        try {
            jDepend.addDirectory(directoryWithClasses);

            Collection<JavaPackage> analyze = jDepend.analyze();

            List<String> errors = new ArrayList<>();
            for (JavaPackage pckg : analyze) {
                for (JavaPackage dependency : (Collection<JavaPackage>) pckg.getEfferents()) {
                    if (!isDependencyAllowed(pckg.getName(), dependency.getName())) {
                        errors.add("Illegal dependency found: " + pckg.getName() + " -> " + dependency.getName());
                    }
                }
            }
            if (! errors.isEmpty()) {
                throw new DependencyVerificationException(errors.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isDependencyAllowed(String fromPackage, String toPackage) {
        return allowedDependencies.get(fromPackage) != null
                && allowedDependencies.get(fromPackage).contains(toPackage);
    }
}
