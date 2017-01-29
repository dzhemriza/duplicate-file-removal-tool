/*
 * org.dfrm
 *
 * File Name: FileChecksumCalculatorService.java
 *
 * Copyright 2017 Dzhem Riza
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dfrt.domain.model.services;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface FileChecksumCalculatorService {

    /**
     * Traverse the whole directory tree and calculates the checksum of all
     * files.
     *
     * @param path
     * @return {@code Map<String, Path>} contains checksum represented as
     *         {@link String} and array of {@link Path} as file.
     */
    Map<String, List<Path>> traverse(Path path);

}
