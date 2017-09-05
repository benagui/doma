/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.jdbc.tx;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that a save point already exists.
 */
public class SavepointAlreadyExistsException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String savepointName;

    public SavepointAlreadyExistsException(String savepointName) {
        super(Message.DOMA2059, savepointName);
        this.savepointName = savepointName;
    }

    public String getSavepointName() {
        return savepointName;
    }
}
