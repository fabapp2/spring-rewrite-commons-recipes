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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.rewrite.boot.autoconfigure.SpringRewriteCommonsConfiguration;
import org.springframework.rewrite.execution.RewriteRecipeLauncher;
import org.springframework.rewrite.parsers.RewriteProjectParser;
import org.springframework.rewrite.project.resource.ProjectResourceSet;
import org.springframework.rewrite.project.resource.ProjectResourceSetFactory;
import org.springframework.rewrite.recipes.RewriteRecipeDiscovery;
import org.springframework.rewrite.support.openrewrite.GenericOpenRewriteRecipe;

/**
 * @author Fabian Krüger
 */
@SpringBootApplication
@Import(SpringRewriteCommonsConfiguration.class)
public class ClientApplication implements ApplicationRunner {

    @Autowired
    private RewriteRecipeDiscovery discovery;
    @Autowired
    private RewriteProjectParser parser;
    @Autowired
    private RewriteRecipeLauncher launcher;
   @Autowired
   private ProjectResourceSetFactory resourceSetFactory;

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        GenericOpenRewriteRecipe recipe = new GenericOpenRewriteRecipe(() -> null);
        ProjectResourceSet projectResourceSet = resourceSetFactory.create(null, null);
    }
}
