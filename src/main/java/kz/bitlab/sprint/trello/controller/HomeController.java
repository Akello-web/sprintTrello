package kz.bitlab.sprint.trello.controller;

import kz.bitlab.sprint.trello.model.Folders;
import kz.bitlab.sprint.trello.model.TaskCategories;
import kz.bitlab.sprint.trello.model.Tasks;
import kz.bitlab.sprint.trello.repository.CategoryRepository;
import kz.bitlab.sprint.trello.repository.CommentsRepository;
import kz.bitlab.sprint.trello.repository.FolderRepository;
import kz.bitlab.sprint.trello.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final FolderRepository folderRepository;
    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;
    private final CommentsRepository commentsRepository;

    @GetMapping(value = "/")
    public String mainPage(Model model){
        model.addAttribute("folders", folderRepository.findAll());
        return "index";
    }

    @PostMapping(value = "/add-folder")
    public String addFolder(Folders folder){
        folderRepository.save(folder);
        return "redirect:/";
    }
    @GetMapping(value = "/details/{folderId}")
    public String folderDetails(@PathVariable(name = "folderId") Long id, Model model){
        Folders folder = folderRepository.findById(id).orElse(null);
        model.addAttribute("folderById", folder);

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("tasks", taskRepository.findAll());
        return "details";
    }

    @GetMapping(value = "/detailsTasks/{taskId}")
    public String taskDetails(@PathVariable(name = "taskId") Long id, Model model){

        Tasks tasks = taskRepository.findById(id).orElse(null);
        model.addAttribute("taskById", tasks);
        return "detailsTasks";
    }

    @PostMapping(value = "/add-task")
    public String addTask(Tasks task, @RequestParam(name = "folder_id") Long folderId){
        task.setStatus(0);
        Folders folder = folderRepository.findById(folderId).orElse(null);
        task.setFolder(folder);
        taskRepository.save(task);
        return "redirect:/details/" + folderId;
    }

    @PostMapping(value = "/save-task")
    public String saveTask(Tasks task, @RequestParam(name = "folder_id") Long folderId){

        Folders folder = folderRepository.findById(folderId).orElse(null);
        task.setFolder(folder);
        taskRepository.save(task);
        return "redirect:/details/" + folderId;
    }

    @PostMapping(value = "/add-category")
    public String addCategory(@RequestParam(name = "folder_id") Long folderId,
                              @RequestParam(name = "category_id") Long categoryId){
        Folders folders = folderRepository.findById(folderId).orElse(null);
        TaskCategories categories = categoryRepository.findById(categoryId).orElse(null);

        if(folders.getTaskCategories()!=null && folders.getTaskCategories().size()>0){
            if(!folders.getTaskCategories().contains(categories)) {
                folders.getTaskCategories().add(categories);
            }
        }else {
            List<TaskCategories> categoriesList = new ArrayList<>();
            categoriesList.add(categories);
            folders.setTaskCategories(categoriesList);
        }

        folderRepository.save(folders);
        return "redirect:/details/" + folderId;
    }

    @PostMapping(value = "/drop-category")
    public String dropCategory(@RequestParam(name = "folder_id") Long folderId,
                              @RequestParam(name = "category_id") Long categoryId){
        Folders folders = folderRepository.findById(folderId).orElse(null);
        TaskCategories categories = categoryRepository.findById(categoryId).orElse(null);

        if(folders.getTaskCategories()!=null && folders.getTaskCategories().size()>0){

            folders.getTaskCategories().remove(categories);

        }

        folderRepository.save(folders);
        return "redirect:/details/" + folderId;
    }

    @PostMapping(value = "/delete-task")
    public String deleteTask(@RequestParam(name = "id") Long id,
                             @RequestParam(name = "folder_id") Long folderId){
        taskRepository.deleteById(id);
        return "redirect:/details/" + folderId;
    }

    @GetMapping(value = "/categories-page")
    public String categoryPage(Model model){
        model.addAttribute("cats", categoryRepository.findAll());
        return "categoryPage";
    }

    @PostMapping(value = "/add-category-db")
    public String addNewCategory(TaskCategories category){
        categoryRepository.save(category);
        return "redirect:/categories-page";
    }

}
