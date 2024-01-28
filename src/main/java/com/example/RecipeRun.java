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
package com.example;

import org.openrewrite.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.rewrite.RewriteProjectParser;
import org.springframework.rewrite.parser.RewriteProjectParsingResult;
import org.springframework.rewrite.resource.ProjectResourceSet;
import org.springframework.rewrite.resource.ProjectResourceSetFactory;
import org.springframework.rewrite.resource.ProjectResourceSetSerializer;

import java.nio.file.Path;

/**
 * @author Fabian Krüger
 */
@SpringBootApplication
public class RecipeRun implements ApplicationRunner {

    @Autowired
    private RewriteProjectParser parser;
    @Autowired
    private ProjectResourceSetFactory resourceSetFactory;
    @Autowired
    private ProjectResourceSetSerializer serializer;


    public static void main(String[] args) {
        SpringApplication.run(RecipeRun.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(args.getNonOptionArgs().isEmpty()) {
            throw new IllegalArgumentException("Path must be provided.");
        }
        Path baseDir = Path.of(args.getNonOptionArgs().get(0)).toAbsolutePath().normalize();
        RewriteProjectParsingResult result = parser.parse(baseDir);
        ProjectResourceSet resourceSet = resourceSetFactory.create(baseDir, result.sourceFiles());
        Recipe recipe = RecipeFactory.create();
        resourceSet.apply(recipe);
        serializer.writeChanges(resourceSet);
    }
}
