package com.stampbot.service.task.provider;

import java.util.List;

public interface TaskServiceProvider {

    List<String> validateIds(List<String> ids) throws Exception;

}
