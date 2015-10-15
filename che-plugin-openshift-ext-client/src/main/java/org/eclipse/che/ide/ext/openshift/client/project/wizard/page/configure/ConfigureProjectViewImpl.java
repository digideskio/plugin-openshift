/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.ext.openshift.client.project.wizard.page.configure;

import elemental.dom.Element;
import elemental.html.SpanElement;
import elemental.html.TableElement;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.ide.Resources;
import org.eclipse.che.ide.ext.openshift.shared.dto.Project;
import org.eclipse.che.ide.ext.openshift.shared.dto.Template;
import org.eclipse.che.ide.ui.list.SimpleList;
import org.eclipse.che.ide.util.dom.Elements;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of view {@link ConfigureProjectView}.
 *
 * @author Vlad Zhukovskiy
 */
@Singleton
public class ConfigureProjectViewImpl implements ConfigureProjectView {

    private static ConfigureProjectViewImplUiBinder uiBinder = GWT.create(ConfigureProjectViewImplUiBinder.class);

    interface ConfigureProjectViewImplUiBinder extends UiBinder<DockPanel, ConfigureProjectViewImpl> {
    }

    @UiField
    TextBox osProjectNameInput;

    @UiField
    TextBox osProjectDisplayNameInput;

    @UiField
    TextArea osProjectDescriptionInput;

    @UiField
    RadioButton osNewProjectButton;

    @UiField
    RadioButton osExistProjectButton;

    @UiField
    TextBox cdProjectNameInput;

    @UiField
    TextArea cdProjectDescriptionInput;

    @UiField
    RadioButton cdPrivateProject;

    @UiField
    RadioButton cdPublicProject;

    @UiField
    ScrollPanel osExistProjectListPanel;

    private SimpleList<Project> projectsList;

    private ActionDelegate delegate;
    private DockPanel      widget;

    @Inject
    public ConfigureProjectViewImpl(Resources resources) {
        widget = uiBinder.createAndBindUi(this);

        TableElement breakPointsElement = Elements.createTableElement();
        breakPointsElement.setAttribute("style", "width: 100%");

        projectsList = SimpleList.create((SimpleList.View)breakPointsElement, resources.defaultSimpleListCss(),
                                         new SimpleList.ListItemRenderer<Project>() {
                                             @Override
                                             public void render(Element listItemBase, Project itemData) {
                                                 //TODO rework this method to proper display each project name
                                                 SpanElement container = Elements.createSpanElement();
                                                 container.setInnerText(itemData.getMetadata().getName());

                                                 listItemBase.appendChild(container);
                                             }
                                         },
                                         new SimpleList.ListEventDelegate<Project>() {
                                             @Override
                                             public void onListItemClicked(Element listItemBase, Project itemData) {
                                                 if (osExistProjectButton.getValue()) {
                                                     projectsList.getSelectionModel().setSelectedItem(itemData);
                                                     delegate.onExistProjectSelected();
                                                 }
                                             }

                                             @Override
                                             public void onListItemDoubleClicked(Element listItemBase, Project itemData) {

                                             }
                                         });

        osExistProjectListPanel.add(projectsList);
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public Widget asWidget() {
        return widget;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNewOpenShiftProjectSelected() {
        return osNewProjectButton.getValue();
    }

    @UiHandler({"osNewProjectButton", "osExistProjectButton"})
    public void onRadioButtonClicked(ClickEvent event) {
        final boolean enabled = osNewProjectButton.getValue();

        osProjectNameInput.setEnabled(enabled);
        osProjectDisplayNameInput.setEnabled(enabled);
        osProjectDescriptionInput.setEnabled(enabled);

        if (enabled) {
            projectsList.getSelectionModel().clearSelection();
            delegate.onExistProjectSelected();
        }
    }

    @UiHandler({"osProjectNameInput"})
    public void onOpenShiftProjectNameChanged(KeyUpEvent event) {
        delegate.onOpenShiftNewProjectNameChanged();
    }

    @UiHandler({"cdProjectNameInput"})
    public void onCodenvyProjectNameChanged(KeyUpEvent event) {
        delegate.onCodenvyNewProjectNameChanged();
    }

    /** {@inheritDoc} */
    @Override
    public Project getExistedSelectedProject() {
        return projectsList.getSelectionModel().getSelectedItem();
    }

    /** {@inheritDoc} */
    @Override
    public void resetControls() {
        osProjectNameInput.setValue("", true);
        osProjectDisplayNameInput.setValue("", true);
        osProjectDescriptionInput.setValue("", true);
        osNewProjectButton.setValue(Boolean.TRUE, true);

        cdProjectNameInput.setValue("", true);
        cdProjectDescriptionInput.setValue("", true);
        cdPublicProject.setValue(Boolean.TRUE, true);

        projectsList.render(Collections.<Project>emptyList());
    }

    /** {@inheritDoc} */
    @Override
    public void setExistOpenShiftProjects(List<Project> projects) {
        projectsList.render(projects);
    }

    /** {@inheritDoc} */
    @Override
    public String getOpenShiftNewProjectName() {
        return osProjectNameInput.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public String getCodenvyNewProjectName() {
        return cdProjectNameInput.getValue();
    }
}
