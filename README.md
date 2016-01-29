# lewis
Rock your Android

## Purpose

This is an extension for Android Lint, adding new rules:
* Every .java file must be inside a custom package, not inside the root package. WARNING

## Getting started

#### Clone the repository
```bash
git clone https://github.com/inaka/lewis.git
```

#### Enter to the project
```bash
cd lewis/
```

#### Build it
```bash
./gradlew build
```

#### Copy the .jar file to the lint folder
```bash
cp ./build/libs/lewis.jar ~/.android/lint/
```

#### Verify whether the issues are registered with lint
```bash
lint --show RootPackageDetector
```

#### Run lint
```bash
./gradlew lint
```
>   Note: If you can't run lint directly, you may want to include android tools PATH in your ~/.bash_profile. (i.e. PATH=$PATH:~/Library/Android/sdk/tools)
>
>    Then run source ~/.bash_profile.
