package ru.otus.appcontainer;
import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        Arrays.stream(initialConfigClasses)
                .sorted(Comparator.comparingInt(config ->
                        config.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }

    public AppComponentsContainerImpl(String configPath) {
        Reflections reflections = new Reflections(configPath);
        reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class)
                .stream().sorted(Comparator.comparingInt(config ->
                        config.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);

    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object config;
        try {
            config = configClass.getConstructor().newInstance();
            var annotatedMethods = Arrays.stream(configClass.getMethods())
                    .filter(method -> method.isAnnotationPresent(AppComponent.class)).toList();

            Set<Class<?>> distinctComponents = new HashSet<>();

            for (Method method : annotatedMethods) {
                Class<?> returnType = method.getReturnType();
                if (!distinctComponents.add(returnType)) {
                    throw new IllegalArgumentException("Duplicate components found.");
                }
            }
            var sortedMethods = annotatedMethods.stream().sorted(Comparator.comparingInt(method ->
                    method.getAnnotation(AppComponent.class).order())).toList();

            sortedMethods.forEach(method -> {
                try {
                    String componentName = method.getAnnotation(AppComponent.class).name();
                    var params = method.getParameterTypes();
                    List<Object> components = new ArrayList<>();
                    for (var param : params) {
                        components.add(getAppComponent(param));
                    }
                    Object component = method.invoke(config, components.toArray());
                    if (!appComponents.contains(component)) {
                        appComponents.add(component);
                    }
                    if (!appComponentsByName.containsKey(componentName)) {
                        appComponentsByName.put(componentName, component);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }


    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> foundComponents = new ArrayList<>();
        for (Object component : appComponents) {
            if (componentClass.isInstance(component)) {
                foundComponents.add(component);
            }
        }
        if (foundComponents.size() != 1) {
            throw new IllegalArgumentException(String.format("Component %s is missing.", componentClass.getName()));
        }
        return (C) foundComponents.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            Object component = appComponentsByName.get(componentName);
            return (C) component;
        } else {
            throw new IllegalArgumentException(String.format("Component %s is missing.", componentName));
        }
    }
}
