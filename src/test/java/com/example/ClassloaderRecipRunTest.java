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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.Recipe;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

/**
 * @author Fabian Krüger
 */
public class ClassloaderRecipRunTest {

    @Test
    @DisplayName("shouldRunRecipe")
    void shouldRunRecipe() throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        Path jar = Path.of("target/spring.rewrite-commons-recipes-1.0-SNAPSHOT.jar");
        File jarFile = jar.toFile();
        URL[] urls = {jarFile.toURI().toURL()};
        URLClassLoader classLoader = new URLClassLoader(urls);

        Class<?> aClass = classLoader.loadClass("com.example.RecipeFactory");
        Object newInstance = aClass.getConstructor().newInstance();

        Method getMessage = aClass.getDeclaredMethod("create");
        Object returned = getMessage.invoke(newInstance);
        if (!Recipe.class.isInstance(returned)) {
            throw new IllegalArgumentException("The method does not return a Recipe. Type was: " + returned.getClass().getName());
        }
        Recipe recipe = (Recipe) returned;
    }
}
