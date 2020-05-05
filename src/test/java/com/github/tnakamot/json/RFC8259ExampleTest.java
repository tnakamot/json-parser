package com.github.tnakamot.json;

import com.github.tnakamot.json.parser.JSONParserException;

import com.github.tnakamot.json.value.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RFC8259ExampleTest {
  private static final Logger log = LoggerFactory.getLogger(RFC8259ExampleTest.class);
  private static final String resourceBase = "/com/github/tnakamot/json/rfc8259/";

  @Test
  public void testExample1() throws IOException, JSONParserException {
    URL example = this.getClass().getResource(resourceBase + "rfc8259_example1.json");
    JSONText jsText = JSONText.fromURL(example);
    assertEquals("rfc8259_example1.json", jsText.name());

    JSONValue root = jsText.parse();

    assertEquals(JSONValueType.OBJECT, root.type());
    assertTrue(root instanceof JSONValueObject);
    JSONValueObject rootObj = (JSONValueObject) root;
    assertEquals(1, rootObj.size());

    assertTrue(rootObj.containsKey("Image"));
    JSONValue image = rootObj.get("Image");
    assertEquals(JSONValueType.OBJECT, image.type());
    assertTrue(image instanceof JSONValueObject);
    JSONValueObject imageObj = (JSONValueObject) image;
    assertEquals(6, imageObj.size());

    assertTrue(imageObj.containsKey("Width"));
    JSONValue width = imageObj.get("Width");
    assertEquals(JSONValueType.NUMBER, width.type());
    assertTrue(width instanceof JSONValueNumber);
    JSONValueNumber widthNum = (JSONValueNumber) width;
    assertTrue(widthNum.canBeLong());
    assertEquals(800, widthNum.toLong());

    assertTrue(imageObj.containsKey("Height"));
    JSONValue height = imageObj.get("Height");
    assertEquals(JSONValueType.NUMBER, height.type());
    assertTrue(height instanceof JSONValueNumber);
    JSONValueNumber heightNum = (JSONValueNumber) height;
    assertTrue(heightNum.canBeLong());
    assertEquals(600, heightNum.toLong());

    assertTrue(imageObj.containsKey("Title"));
    JSONValue title = imageObj.get("Title");
    assertEquals(JSONValueType.STRING, title.type());
    assertTrue(title instanceof JSONValueString);
    JSONValueString titleStr = (JSONValueString) title;
    assertEquals("View from 15th Floor", titleStr.value());

    assertTrue(imageObj.containsKey("Animated"));
    JSONValue animated = imageObj.get("Animated");
    assertEquals(JSONValueType.BOOLEAN, animated.type());
    assertTrue(animated instanceof JSONValueBoolean);
    JSONValueBoolean animatedBool = (JSONValueBoolean) animated;
    assertFalse(animatedBool.value());

    assertTrue(imageObj.containsKey("IDs"));
    JSONValue ids = imageObj.get("IDs");
    assertEquals(JSONValueType.ARRAY, ids.type());
    assertTrue(ids instanceof JSONValueArray);
    JSONValueArray idsArray = (JSONValueArray) ids;
    assertEquals(4, idsArray.size());
    long[] expectedIds = {116, 943, 234, 38793};
    int i = 0;
    for (JSONValue id : idsArray) {
      assertEquals(JSONValueType.NUMBER, id.type());
      assertTrue(id instanceof JSONValueNumber);
      JSONValueNumber idNum = (JSONValueNumber) id;
      assertTrue(idNum.canBeLong());
      assertEquals(expectedIds[i++], idNum.toLong());
    }

    assertTrue(imageObj.containsKey("Thumbnail"));
    JSONValue thumbnail = imageObj.get("Thumbnail");
    assertEquals(JSONValueType.OBJECT, thumbnail.type());
    assertTrue(thumbnail instanceof JSONValueObject);
    JSONValueObject thumbnailObj = (JSONValueObject) thumbnail;
    assertEquals(3, thumbnailObj.size());

    assertTrue(thumbnailObj.containsKey("Url"));
    JSONValue url = thumbnailObj.get("Url");
    assertEquals(JSONValueType.STRING, url.type());
    assertTrue(url instanceof JSONValueString);
    JSONValueString urlStr = (JSONValueString) url;
    assertEquals("http://www.example.com/image/481989943", urlStr.value());

    assertTrue(thumbnailObj.containsKey("Height"));
    JSONValue thumbnailHeight = thumbnailObj.get("Height");
    assertEquals(JSONValueType.NUMBER, thumbnailHeight.type());
    assertTrue(thumbnailHeight instanceof JSONValueNumber);
    JSONValueNumber thumbnailHeightNum = (JSONValueNumber) thumbnailHeight;
    assertTrue(thumbnailHeightNum.canBeLong());
    assertEquals(125, thumbnailHeightNum.toLong());

    assertTrue(thumbnailObj.containsKey("Width"));
    JSONValue thumbnailWidth = thumbnailObj.get("Width");
    assertEquals(JSONValueType.NUMBER, thumbnailWidth.type());
    assertTrue(thumbnailWidth instanceof JSONValueNumber);
    JSONValueNumber thumbnailWidthNum = (JSONValueNumber) thumbnailWidth;
    assertTrue(thumbnailWidthNum.canBeLong());
    assertEquals(100, thumbnailWidthNum.toLong());
  }

  @Test
  public void testExample2() throws IOException, JSONParserException {
    Map<String, JSONValue> expected1 =
        new HashMap<>() {
          {
            put("precision", new JSONValueString("zip"));
            put("Latitude", new JSONValueNumber("37.7668"));
            put("Longitude", new JSONValueNumber("-122.3959"));
            put("Address", new JSONValueString(""));
            put("City", new JSONValueString("SAN FRANCISCO"));
            put("State", new JSONValueString("CA"));
            put("Zip", new JSONValueString("94107"));
            put("Country", new JSONValueString("US"));
          }
        };

    Map<String, JSONValue> expected2 =
        new HashMap<>() {
          {
            put("precision", new JSONValueString("zip"));
            put("Latitude", new JSONValueNumber("37.371991"));
            put("Longitude", new JSONValueNumber("-122.026020"));
            put("Address", new JSONValueString(""));
            put("City", new JSONValueString("SUNNYVALE"));
            put("State", new JSONValueString("CA"));
            put("Zip", new JSONValueString("94085"));
            put("Country", new JSONValueString("US"));
          }
        };

    Map<String, JSONValue>[] expected = new Map[] {expected1, expected2};

    URL example = this.getClass().getResource(resourceBase + "rfc8259_example2.json");
    JSONText jsText = JSONText.fromURL(example);
    assertEquals("rfc8259_example2.json", jsText.name());

    JSONValue root = jsText.parse();

    assertEquals(JSONValueType.ARRAY, root.type());
    assertTrue(root instanceof JSONValueArray);
    JSONValueArray rootArray = (JSONValueArray) root;
    assertEquals(2, rootArray.size());
    int i = 0;

    for (JSONValue value : rootArray) {
      assertEquals(JSONValueType.OBJECT, value.type());
      assertTrue(value instanceof JSONValueObject);
      JSONValueObject valueObj = (JSONValueObject) value;

      assertEquals(expected[i].size(), valueObj.size());

      for (Map.Entry<String, JSONValue> entry : expected[i].entrySet()) {
        String key = entry.getKey();
        JSONValue expectedVal = entry.getValue();

        assertTrue(valueObj.containsKey(key));
        assertTrue(
            expectedVal instanceof JSONValueNumber || expectedVal instanceof JSONValueString);
        assertEquals(expectedVal, valueObj.get(key));
      }

      i++;
    }
  }

  @Test
  public void testExample3() throws IOException, JSONParserException {
    URL example = this.getClass().getResource(resourceBase + "rfc8259_example3.json");
    JSONText jsText = JSONText.fromURL(example);
    assertEquals("rfc8259_example3.json", jsText.name());

    JSONValue root = jsText.parse();

    assertEquals(JSONValueType.STRING, root.type());
    assertTrue(root instanceof JSONValueString);
    JSONValueString rootStr = (JSONValueString) root;
    assertEquals("Hello world!", rootStr.value());
  }

  @Test
  public void testExample4() throws IOException, JSONParserException {
    URL example = this.getClass().getResource(resourceBase + "rfc8259_example4.json");
    JSONText jsText = JSONText.fromURL(example);
    assertEquals("rfc8259_example4.json", jsText.name());

    JSONValue root = jsText.parse();

    assertEquals(JSONValueType.NUMBER, root.type());
    assertTrue(root instanceof JSONValueNumber);
    JSONValueNumber rootNum = (JSONValueNumber) root;
    assertTrue(rootNum.canBeLong());
    assertEquals(42, rootNum.toLong());
  }

  @Test
  public void testExample5() throws IOException, JSONParserException {
    URL example = this.getClass().getResource(resourceBase + "rfc8259_example5.json");
    JSONText jsText = JSONText.fromURL(example);
    assertEquals("rfc8259_example5.json", jsText.name());

    JSONValue root = jsText.parse();

    assertEquals(JSONValueType.BOOLEAN, root.type());
    assertTrue(root instanceof JSONValueBoolean);
    JSONValueBoolean rootBool = (JSONValueBoolean) root;
    assertTrue(rootBool.value());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "rfc8259_example1.json",
        "rfc8259_example2.json",
        "rfc8259_example3.json",
        "rfc8259_example4.json",
        "rfc8259_example5.json",
      })
  public void testOutput(String fileName) throws IOException, JSONParserException {
    URL example = this.getClass().getResource(resourceBase + fileName);
    JSONText jsText = JSONText.fromURL(example);
    JSONValue root = jsText.parse();

    // Although it is not necessary to exactly match the output of
    // toTokenString() with the original JSON text, it is a good
    // test to check if the original order in the JSON objects are
    // retained and to check if toTokenString() works as intended.
    String output = root.toTokenString("\n", "  ");
    log.info(() -> fileName + " => \n" + output);

    jsText.get().equals(output);
  }
}
