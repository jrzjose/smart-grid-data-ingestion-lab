# project init

create initial image
```shell
    podman build -t ng-ui-tmp .
```

run the container
```shell
    podman run -d -p 8092:80 --name ng-ui-tmp-cntr -v /tmp:/workspace:rw,Z ng-ui-tmp
```

Using podman terminal
```shell
    ng new ng-ui
```

add angular material
```shell
    ng add @angular/material
```



