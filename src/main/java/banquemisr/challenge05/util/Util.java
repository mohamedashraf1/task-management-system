package banquemisr.challenge05.util;

import banquemisr.challenge05.constants.Constant;
import banquemisr.challenge05.errorhandling.BusinessException;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class Util {
    public static Long getLoggedUserId() {
        String userId = ThreadContext.get(Constant.USER_ID);
        if (userId != null && !userId.isEmpty()) {
            return Long.valueOf(userId);
        }
        throw new BusinessException("userId not available");
    }


    public static <A, B> List<B> mapList(List<A> aList, Class<B> bClass) {
        List<B> bList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for (A a : aList) {
            try {
                bList.add(modelMapper.map(a, bClass));
            } catch (Exception e) {
                log.error("error while mapping " + aList.getClass() + " to " + bClass);
                log.error(e.toString());
            }
        }

        return bList;
    }
}
