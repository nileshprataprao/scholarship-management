package edu.ua.scholarship.matchengine;


import edu.ua.scholarship.entity.Scholarship;
import edu.ua.scholarship.entity.Student;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ScholarshipScoreCalculator {


    public static boolean isEligible(Student student, Scholarship scholarship) {
            return scholarship.getMajors().contains(student.getMajor()) &&
                    student.getGpa() >= scholarship.getGpa() ;
        }

    public static int calculateScore(Student student, Scholarship scholarship) {
        int score = 0;


        if(student.getGpa() >= scholarship.getGpa()) {
            score += 1;
        }

        if (scholarship.getMajors() != null && !scholarship.getMajors().isEmpty() && scholarship.getMajors().contains(student.getMajor())) {
           score += 1;
        }

        if (scholarship.getMinors()!= null && !scholarship.getMinors().isEmpty() && scholarship.getMinors().contains(student.getMinor())) {
            score += 1;
        }

       double essayQualityScore = evaluateEssayQualityScore(student.getEssay());
        if((1.0 - essayQualityScore) <= .5){
            score += 1;
        }
        return score;
    }

    private static double evaluateEssayQualityScore(String essayText) {
        final double minEssayScore = 5;
        final int expectedChars= 500;
        if (essayText == null || essayText.trim().isEmpty()) return 0;

        int lengthScore = Math.min(essayText.length() / expectedChars, 1);
        String[] words = essayText.split("\\s+");
        Set<String> uniqueWords = new HashSet<>(Arrays.asList(words));
        double vocabScore = (double) uniqueWords.size() / words.length;

        double score = (lengthScore * 0.1 + vocabScore * 0.9) * 10;
        double essayQuality = Math.min(score, 10.0);
        return Math.min(essayQuality / minEssayScore, 1.0);

    }




    }





