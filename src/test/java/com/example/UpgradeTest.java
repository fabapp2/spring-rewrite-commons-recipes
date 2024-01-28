package com.example;/*
 * Copyright 2021 - 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.RecipeRun;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.rewrite.RewriteProjectParser;
import org.springframework.rewrite.RewriteRecipeDiscovery;
import org.springframework.rewrite.boot.autoconfigure.RewriteLauncherConfiguration;
import org.springframework.rewrite.parser.RewriteProjectParsingResult;
import org.springframework.rewrite.resource.ProjectResourceSetFactory;
import org.springframework.rewrite.resource.ProjectResourceSetSerializer;
import org.springframework.rewrite.test.util.TestProjectHelper;

import java.nio.file.Path;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
@SpringBootTest(classes = { RewriteLauncherConfiguration.class })
public class UpgradeTest {

    @Autowired
    private RewriteProjectParser parser;
    @Autowired
    private RewriteRecipeDiscovery discovery;
    @Autowired
    private ExecutionContext executionContext;
    @Autowired
    private ProjectResourceSetSerializer serializer;
    @Autowired
    private ProjectResourceSetFactory resourceSetFactory;

    @Test
    @DisplayName("should rename parsers package")
    void shouldRenameParsersPackage() {
        Path baseDir = TestProjectHelper.getMavenProject("client-project");
        RewriteProjectParsingResult parseResult = parser.parse(baseDir);
        Recipe recipe = RecipeFactory.create();
        assertThat(recipe).isNotNull();
        RecipeRun recipeRun = recipe.run(new InMemoryLargeSourceSet(parseResult.sourceFiles()), executionContext);
        String diffs = recipeRun.getChangeset().getAllResults().stream()
                .map(r -> r.diff())
                .collect(Collectors.joining());
        assertThat(diffs)
                .contains("import org.springframework.rewrite.RewriteRecipeDiscovery;")
                .contains("import org.springframework.rewrite.RewriteRecipeLauncher;")
                .contains("import org.springframework.rewrite.RewriteProjectParser;")
                .contains("import org.springframework.rewrite.boot.autoconfigure.RewriteLauncherConfiguration;")
                .contains("import org.springframework.rewrite.recipes.GenericOpenRewriteRecipe;")
                .contains("import org.springframework.rewrite.resource.ProjectResourceSet;")
                .contains("import org.springframework.rewrite.resource.ProjectResourceSetFactory;")
                .contains("+@Import(RewriteLauncherConfiguration.class)");
    }
    
}
