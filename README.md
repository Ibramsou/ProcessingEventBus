# Processing Event Bus
Like event bus of guava, ProcessingEventBus is a lightweight library that permit you to post events.

The only different is that Reflection isn't used to post and register events.

Unlike guava, ProcessingEventBus generate source files in compilation task with annotation processor unstead of using runtime reflection

# Getting started
Installation with gradle
```groovy
annotationProcessor 'io.github.ibramsou:processing-event-bus:1.0.0'
implementation 'io.github.ibramsou:processing-event-bus:1.0.0'
```

# How to use ?
```java
// Create a new instance of Event Bus
public class Main {
  public static void main(String[] args) {
    EventBus bus = new EventBus();
    // Register your event listener handler
    bus.register(new EventListener());
    // Post your events
    bus.post(new MessageEvent("Hello world !"));
  }
}

// Create an event listener handler
@Handler
public class EventListener {
  @Subscribe
  public void onEvent(MessageEvent event) {
    System.out.println(event.getMessage());
  }
  
  @Subscribe(priority = EventPriority.HIGH)
  public void onFirstPost(MessageEvent event) {
    event.setMessage("[Here is my prefix] " + event.getMessage());
  }
}

// Create an event
public class MessageEvent {
  private String message;
  
  public MessageEvent(String message) {
    this.message = message;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }
}
```
