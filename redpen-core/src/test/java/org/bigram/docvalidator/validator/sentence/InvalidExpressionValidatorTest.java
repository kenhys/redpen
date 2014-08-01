/**
 * redpen: a text inspection tool
 * Copyright (C) 2014 Recruit Technologies Co., Ltd. and contributors
 * (see CONTRIBUTORS.md)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bigram.docvalidator.validator.sentence;

import org.bigram.docvalidator.DocumentValidator;
import org.bigram.docvalidator.DocumentValidatorException;
import org.bigram.docvalidator.ValidationError;
import org.bigram.docvalidator.config.Configuration;
import org.bigram.docvalidator.config.ValidatorConfiguration;
import org.bigram.docvalidator.distributor.FakeResultDistributor;
import org.bigram.docvalidator.model.DocumentCollection;
import org.bigram.docvalidator.model.Sentence;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class InvalidExpressionValidatorTest {

  @Test
  public void testSimpleRun() {
    InvalidExpressionValidator validator = new InvalidExpressionValidator();
    validator.addInvalid("may");
    List<ValidationError> errors = validator.validate(new Sentence("The experiments may be true.", 0));
    assertEquals(1, errors.size());
  }

  @Test
  public void testVoid() {
    InvalidExpressionValidator validator = new InvalidExpressionValidator();
    validator.addInvalid("may");
    List<ValidationError> errors = validator.validate(new Sentence("", 0));
    assertEquals(0, errors.size());
  }

  @Test
  public void testLoadDefaultDictionary() throws DocumentValidatorException {
    Configuration config = new Configuration.Builder()
        .addValidatorConfig(new ValidatorConfiguration("InvalidExpression"))
        .setCharacterTable("en").build();

    DocumentCollection documents = new DocumentCollection.Builder()
        .addDocument("")
        .addSection(1, new ArrayList<>())
        .addParagraph()
        .addSentence(
            "You know. He is a super man.",
            1)
        .build();

    DocumentValidator validator = new DocumentValidator.Builder()
        .setConfiguration(config)
        .setResultDistributor(new FakeResultDistributor())
        .build();

    List<ValidationError> errors = validator.check(documents);
    assertEquals(1, errors.size());
  }

  @Test
  public void testLoadJapaneseDefaultDictionary() throws DocumentValidatorException {
    Configuration config = new Configuration.Builder()
        .addValidatorConfig(new ValidatorConfiguration("InvalidExpression"))
        .setCharacterTable("ja").build();

    DocumentCollection documents = new DocumentCollection.Builder()
        .addDocument("")
        .addSection(1, new ArrayList<>())
        .addParagraph()
        .addSentence(
            "明日地球が滅亡するってマジですか。",
            1)
        .build();

    DocumentValidator validator = new DocumentValidator.Builder()
        .setConfiguration(config)
        .setResultDistributor(new FakeResultDistributor())
        .build();

    List<ValidationError> errors = validator.check(documents);
    assertEquals(1, errors.size());
  }
}