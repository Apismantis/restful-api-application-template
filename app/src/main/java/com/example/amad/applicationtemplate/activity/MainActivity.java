package com.example.amad.applicationtemplate.activity;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.widget.Toast;

import com.example.amad.applicationtemplate.R;
import com.example.amad.applicationtemplate.model.Project;

import java.util.Date;
import java.util.List;

import butterknife.OnClick;

public class MainActivity extends AMADActivity {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.btnCreateProject)
    void onClickedCreateProjectButton() {
        createProject();
    }

    @OnClick(R.id.btnReadProject)
    void onClickedReadProjectButton() {
        readProject();
    }

    @OnClick(R.id.btnReadManyProjects)
    void onClickedReadManyProjectsButton() {
        readManyProjects();
    }

    @OnClick(R.id.btnUpdateProject)
    void onClickedUpdateProjectButton() {
        updateProject();
    }

    @OnClick(R.id.btnDeleteProject)
    void onClickedDeleteProjectButton() {
        deleteProject();
    }

    private CreateProjectDelegate createProjectDelegate;
    private ReadProjectDelegate readProjectDelegate;
    private ReadManyProjectsDelegate readManyProjectsDelegate;
    private UpdateProjectDelegate updateProjectDelegate;
    private DeleteProjectDelegate deleteProjectDelegate;

    private Project readProject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createProjectDelegate = new CreateProjectDelegate(this);
        listOfForegroundTaskDelegates.add(createProjectDelegate);

        readProjectDelegate = new ReadProjectDelegate(this);
        listOfForegroundTaskDelegates.add(readProjectDelegate);

        readManyProjectsDelegate = new ReadManyProjectsDelegate(this);
        listOfForegroundTaskDelegates.add(readManyProjectsDelegate);

        updateProjectDelegate = new UpdateProjectDelegate(this);
        listOfForegroundTaskDelegates.add(updateProjectDelegate);

        deleteProjectDelegate = new DeleteProjectDelegate(this);
        listOfForegroundTaskDelegates.add(deleteProjectDelegate);
    }

    private void createProject() {
        Project project = new Project();
        project.setName("Any Name");
        project.setCreatedDate(new Date());
        project.setDescription("Any Description");
        amadService.createProject(project, createProjectDelegate);
    }

    private void readProject() {
        amadService.readProject(3L, readProjectDelegate);
    }

    private void readManyProjects() {
        amadService.readManyProjects(1, 128, readManyProjectsDelegate);
    }

    private void updateProject() {
        if (readProject != null) {
            readProject.setName("New Name");
            amadService.updateProject(readProject, updateProjectDelegate);
        } else {
            Toast.makeText(this, "Please make sure you have read a project succeeded", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteProject() {
        if (readProject != null) {
            amadService.deleteProject(readProject.getId(), deleteProjectDelegate);
        } else {
            Toast.makeText(this, "Please make sure you have read a project succeeded", Toast.LENGTH_SHORT).show();
        }
    }

    private static class CreateProjectDelegate extends ForegroundTaskDelegate<Project> {

        public CreateProjectDelegate(AMADActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Project project, Throwable throwable) {
            super.onPostExecute(project, throwable);

            if(throwable == null && project != null && shouldHandleResultForActivity()) {
                MainActivity activity = (MainActivity) activityWeakReference.get();
                activity.readProject = project;
                String msg = String.format("Create project (%s, %s) succeeded",
                        activity.readProject.getId(),
                        activity.readProject.getName());
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class ReadProjectDelegate extends ForegroundTaskDelegate<Project> {

        public ReadProjectDelegate(AMADActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Project project, Throwable throwable) {
            super.onPostExecute(project, throwable);

            if(throwable == null && project != null && shouldHandleResultForActivity()) {
                MainActivity activity = (MainActivity)activityWeakReference.get();
                activity.readProject = project;
                String msg = String.format("Read project (%s, %s)", project.getId(), project.getName());
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class ReadManyProjectsDelegate extends ForegroundTaskDelegate<List<Project>> {

        public ReadManyProjectsDelegate(AMADActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(List<Project> listOfProjects, Throwable throwable) {
            super.onPostExecute(listOfProjects, throwable);

            if(throwable == null && listOfProjects != null && shouldHandleResultForActivity()) {
                MainActivity activity = (MainActivity)activityWeakReference.get();
                if (listOfProjects.size() > 0) {
                    activity.readProject = listOfProjects.get(0);
                    String msg = String.format("Read first project (%s, %s)",
                            activity.readProject.getId(),
                            activity.readProject.getName());
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                } else {
                    String msg = "Empty project list";
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private static class UpdateProjectDelegate extends ForegroundTaskDelegate<Project> {

        public UpdateProjectDelegate(AMADActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Project project, Throwable throwable) {
            super.onPostExecute(project, throwable);

            if(throwable == null && project != null && shouldHandleResultForActivity()) {
                MainActivity activity = (MainActivity)activityWeakReference.get();
                String msg = String.format("Update project (%s, %s)", project.getId(), project.getName());
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                activity.readProject = project;
            }
        }
    }

    private static class DeleteProjectDelegate extends ForegroundTaskDelegate<Boolean> {

        public DeleteProjectDelegate(AMADActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Boolean result, Throwable throwable) {
            super.onPostExecute(result, throwable);

            if(throwable == null && result && shouldHandleResultForActivity()) {
                MainActivity activity = (MainActivity)activityWeakReference.get();
                Toast.makeText(activity, "Delete succeeded", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
