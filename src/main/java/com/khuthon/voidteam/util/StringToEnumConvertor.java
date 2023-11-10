package com.khuthon.voidteam.util;


import com.khuthon.voidteam.domain.Grade;
import com.khuthon.voidteam.domain.Job;
import com.khuthon.voidteam.domain.Role;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumConvertor {

    public Job getJobType(String job){
        if(Job.HIGHSCHOOL_STUDENT.getJob().equals(job)){
            return Job.HIGHSCHOOL_STUDENT;
        }
        else if(Job.UNIVERSITY_STUDENT.getJob().equals(job)){
            return Job.UNIVERSITY_STUDENT;
        }
        else if(Job.REPEAT_STUDENT.getJob().equals(job)){
            return Job.REPEAT_STUDENT;
        }
        else {
            return Job.ETC;
        }
    }

    public Role getRoleType(String role){
        if(Role.UNIV.getValue().equals(role)){
            return Role.UNIV;
        }
        else{
            return Role.USER;
        }
    }

    public Grade getGradeType(String grade){
        if(Grade.GOLD.getGrade().equals(grade)){
            return Grade.GOLD;
        }
        else if (Grade.BRONZE.getGrade().equals(grade)){
            return Grade.BRONZE;
        }
        else if(Grade.SILVER.getGrade().equals(grade)){
            return Grade.SILVER;
        }
        else{
            return  Grade.IRON;
        }
    }


}
