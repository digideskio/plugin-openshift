/*******************************************************************************
 * Copyright (c) 2012-2017 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.ext.openshift.shared.dto;

import org.eclipse.che.dto.shared.DTO;

@DTO
public interface ImageStream {
    ObjectMeta getMetadata();

    void setMetadata(ObjectMeta metadata);

    ImageStream withMetadata(ObjectMeta metadata);

    String getApiVersion();

    void setApiVersion(String apiVersion);

    ImageStream withApiVersion(String apiVersion);

    String getKind();

    void setKind(String kind);

    ImageStream withKind(String kind);

    ImageStreamSpec getSpec();

    void setSpec(ImageStreamSpec spec);

    ImageStream withSpec(ImageStreamSpec spec);

    ImageStreamStatus getStatus();

    void setStatus(ImageStreamStatus status);

    ImageStream withStatus(ImageStreamStatus status);

}
