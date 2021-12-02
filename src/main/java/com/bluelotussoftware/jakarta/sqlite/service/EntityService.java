/*
 * Copyright 2021 John Yeary <jyeary@bluelotussoftware.com>.
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
package com.bluelotussoftware.jakarta.sqlite.service;

import java.util.List;

/**
 *
 * @author John Yeary <jyeary@bluelotussoftware.com>
 * @param <T> The {@code Type} of the object.
 */
public interface EntityService<T> {

    void create(T o);

    T update(T o);

    void delete(T o);

    T find(long id);

    List<T> findAll();

}
