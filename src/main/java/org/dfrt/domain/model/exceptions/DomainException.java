/*
 * org.dfrt
 *
 * File Name: DomainException.java
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
package org.dfrt.domain.model.exceptions;

public class DomainException extends RuntimeException {

    private static final long serialVersionUID = -1675780390978562009L;

    public DomainException(String m) {
        super(m);
    }

    public DomainException(String m, Throwable t) {
        super(m, t);
    }
}
