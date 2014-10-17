/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rocui.jpager.base;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.Writer;

/**
 *
 * @author Jinliang
 */
public class JpageException implements TemplateExceptionHandler {

    @Override
    public void handleTemplateException(TemplateException te, Environment e, Writer writer) throws TemplateException {
        try {
            writer.write("[NODATA]");
            System.out.println(te.getMessage());
        } catch (Exception ez) {
            throw new TemplateException("Failed to print error message. Cause: " + writer, e);
        }
    }
}
