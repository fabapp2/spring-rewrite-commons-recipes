/*
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

import org.apache.maven.project.ProjectBuildingHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.RecipeRun;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.rewrite.boot.autoconfigure.SpringRewriteCommonsConfiguration;
import org.springframework.rewrite.execution.RewriteRecipeLauncher;
import org.springframework.rewrite.parsers.RewriteParserConfiguration;
import org.springframework.rewrite.parsers.RewriteProjectParser;
import org.springframework.rewrite.parsers.RewriteProjectParsingResult;
import org.springframework.rewrite.recipes.RewriteRecipeDiscovery;
import org.springframework.rewrite.test.util.ParserParityTestHelper;
import org.springframework.rewrite.test.util.TestProjectHelper;

import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
@SpringBootTest(classes = { SpringRewriteCommonsConfiguration.class })
public class UpgradeTest {

    @Autowired
    private RewriteProjectParser parser;
    @Autowired
    private RewriteRecipeDiscovery discovery;
    @Autowired
    private ExecutionContext executionContext;

    @Test
    @DisplayName("should rename parsers package")
    void shouldRenameParsersPackage() {
        Path mavenProject = TestProjectHelper.getMavenProject("client-project");
        RewriteProjectParsingResult parseResult = parser.parse(mavenProject);
        Optional<Recipe> recipeByName = discovery.findRecipeByName("org.springframework.rewrite.UpgradeSnapshot");
        assertThat(recipeByName).isNotEmpty();
        Recipe recipe = recipeByName.get();
        RecipeRun recipeRun = recipe.run(new InMemoryLargeSourceSet(parseResult.sourceFiles()), executionContext);
        recipeRun.getChangeset().getAllResults().stream()
                .map(r -> r.diff())
                .forEach(System.out::println);
    }
    
}
