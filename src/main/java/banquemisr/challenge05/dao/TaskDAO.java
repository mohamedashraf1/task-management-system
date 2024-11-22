package banquemisr.challenge05.dao;

import banquemisr.challenge05.dto.TaskDTO;
import banquemisr.challenge05.dto.TaskSearchDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static banquemisr.challenge05.util.Util.getLoggedUserId;
@Component
@Log4j2
public class TaskDAO {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public List<TaskDTO> search(TaskSearchDTO criteria){

        String query = buildQuery(criteria);
        Map<String, Object> queryParams = buildQueryParams(criteria);

        return jdbcTemplate.query(query, queryParams, new BeanPropertyRowMapper<>(TaskDTO.class));
    }

    private String buildQuery(TaskSearchDTO criteria){
        String query = "select t.task_id, t.title, t.description, t.priority, t.due_date,\n" +
                "s.status_name as status\n" +
                " from task t inner join lo_task_status s on t.status_id = s.status_id\n" +
                " inner join user u on u.user_id = t.user_id\n" +
                " where u.user_id = :userId";
        if(criteria.getTitle() != null){
            query += " and lower(t.title) like :title";
        }
        if(criteria.getDescription() != null){
            query += " and lower(t.description) like :description";
        }
        if(criteria.getDueDate() != null){
            query += " and t.due_date = STR_TO_DATE(:dueDate, '%d-%m-%Y')";
        }
        if(criteria.getStatusId() != null){
            query += "  and s.status_id = :statusId";
        }
        if(criteria.getPriority() != null){
            query += "  and t.priority = :priority";
        }

        return query;
    }

    private Map<String, Object> buildQueryParams(TaskSearchDTO criteria) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", getLoggedUserId());
        if(criteria.getTitle() != null){
            params.put("title", "%" + criteria.getTitle().toLowerCase() + "%");
        }
        if(criteria.getDescription() != null){
            params.put("description", "%" + criteria.getDescription().toLowerCase()  + "%");
        }
        if(criteria.getDueDate() != null){
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String dateParsed = sdf.format(criteria.getDueDate());
            params.put("dueDate", dateParsed);
        }
        if(criteria.getStatusId() != null){
            params.put("statusId", criteria.getStatusId());
        }
        if(criteria.getPriority() != null){
            params.put("priority", criteria.getPriority());
        }

        return params;
    }
}
