package ua.vyshnyak.configs;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class RepositoryConfigImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        String repositoryType = System.getProperty("repository.type");
        if ("jdbc".equals(repositoryType)) {
            return new String[] {JdbcTemplateRepositoryConfig.class.getName()};
        } else {
            return new String[] {InMemoryRepositoryConfig.class.getName()};
        }
    }
}
