package snabbdom

import org.scalajs.dom._
import outwatch.dom.Attribute

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSImport, ScalaJSDefined}
import scala.scalajs.js.{UndefOr, |}

@js.native
@JSImport("snabbdom/h", JSImport.Namespace, globalFallback = "h")
object hProvider extends js.Object {
  val default: hFunction = js.native
}

@js.native
trait hFunction extends js.Any {
  def apply(nodeType: String, dataObject: DataObject, text: String): VNodeProxy = js.native
  def apply(nodeType: String, dataObject: DataObject, children: js.Array[VNodeProxy]): VNodeProxy = js.native
}

object hFunction {
  def apply(nodeType: String, dataObject: DataObject, text: String): VNodeProxy = {
    hProvider.default.apply(nodeType, dataObject, text)
  }
  def apply(nodeType: String, dataObject: DataObject, children: js.Array[VNodeProxy]): VNodeProxy = {
    hProvider.default.apply(nodeType, dataObject, children)
  }
}


@ScalaJSDefined
trait Hooks extends js.Object {
  val insert: js.UndefOr[js.Function1[VNodeProxy, Unit]]
  val destroy: js.UndefOr[js.Function1[VNodeProxy, Unit]]
  val update: js.UndefOr[js.Function2[VNodeProxy, VNodeProxy, Unit]]
}

object Hooks {
  def apply(insert: js.UndefOr[js.Function1[VNodeProxy, Unit]] = js.undefined,
            destroy: js.UndefOr[js.Function1[VNodeProxy, Unit]] = js.undefined,
            update: js.UndefOr[js.Function2[VNodeProxy, VNodeProxy, Unit]] = js.undefined
           ): Hooks = {
    val _insert = insert
    val _destroy = destroy
    val _update = update
    new Hooks {
      val insert = _insert
      val destroy = _destroy
      val update = _update
    }
  }
}

@ScalaJSDefined
trait DataObject extends js.Object {
  import DataObject._

  val attrs: js.Dictionary[AttrValue]
  val props: js.Dictionary[PropValue]
  val style: js.Dictionary[String]
  val on: js.Dictionary[js.Function1[Event, Unit]]
  val hook: Hooks
  val key: js.UndefOr[KeyValue]
}

object DataObject {

  type PropValue = Any
  type AttrValue = String | Boolean
  type KeyValue = String | Double  // https://github.com/snabbdom/snabbdom#key--string--number

  def apply(attrs: js.Dictionary[AttrValue],
            on: js.Dictionary[js.Function1[Event, Unit]]
           ): DataObject = apply(attrs, js.Dictionary.empty, js.Dictionary.empty, on, Hooks(), js.undefined)


  def apply(attrs: js.Dictionary[AttrValue],
            props: js.Dictionary[PropValue],
            style: js.Dictionary[String],
            on: js.Dictionary[js.Function1[Event, Unit]],
            hook: Hooks,
            key: js.UndefOr[KeyValue]
           ): DataObject = {

    val _attrs = attrs
    val _props = props
    val _style = style
    val _on = on
    val _hook = hook
    val _key = key

    new DataObject {
      val attrs: js.Dictionary[AttrValue] = _attrs
      val props: js.Dictionary[PropValue] = _props
      val style: js.Dictionary[String] = _style
      val on: js.Dictionary[js.Function1[Event, Unit]] = _on
      val hook: Hooks = _hook
      val key: UndefOr[KeyValue] = _key
    }
  }

  def create(attrs: js.Dictionary[AttrValue],
             props: js.Dictionary[PropValue],
             style: js.Dictionary[String],
             on: js.Dictionary[js.Function1[Event, Unit]],
             insert: js.Function1[VNodeProxy, Unit],
             destroy: js.Function1[VNodeProxy, Unit],
             update: js.Function2[VNodeProxy, VNodeProxy, Unit],
             key: js.UndefOr[KeyValue]
            ): DataObject = {

    DataObject(
      attrs = attrs,
      props = props,
      style = style,
      on = on,
      hook = Hooks(insert = insert, destroy = destroy, update = update),
      key = key
    )
  }

  implicit class DataObjectExt(val obj: DataObject) extends AnyVal {

    def withUpdatedAttributes(attributes: Seq[Attribute]): DataObject = {
      import scala.scalajs.js.JSConverters._

      val (attrs, props, style) = VDomProxy.attrsToSnabbDom(attributes)

      val newAttrs = (obj.attrs ++ attrs).toJSDictionary
      val newProps = (obj.props ++ props).toJSDictionary
      val newStyle = (obj.style ++ style).toJSDictionary
      DataObject(attrs = newAttrs, props = newProps, style = newStyle, on = obj.on, hook = obj.hook, key = obj.key)
    }
  }
}

object patch {

  private lazy val p = Snabbdom.init(js.Array(
    SnabbdomClass.default,
    SnabbdomEventListeners.default,
    SnabbdomAttributes.default,
    SnabbdomCustomProps.default,
    SnabbdomStyle.default
  ))

  def apply(firstNode: VNodeProxy, vNode: VNodeProxy): Unit = p(firstNode,vNode)

  def apply(firstNode: org.scalajs.dom.Element, vNode: VNodeProxy): Unit = p(firstNode,vNode)
}

@js.native
trait VNodeProxy extends js.Object {
  val elm: js.UndefOr[Element]
  val data: DataObject
  val children: js.Array[VNodeProxy]
  val sel: String
}

object VNodeProxy {
  def fromString(string: String): VNodeProxy = string.asInstanceOf[VNodeProxy]
}


@js.native
@JSImport("snabbdom", JSImport.Namespace, globalFallback = "snabbdom")
object Snabbdom extends js.Object {
  def init(args: js.Array[Any]): js.Function2[Node | VNodeProxy, VNodeProxy, Unit] = js.native

}

@js.native
@JSImport("snabbdom/modules/class", JSImport.Namespace, globalFallback = "snabbdom_class")
object SnabbdomClass extends js.Object {
  val default: js.Any = js.native
}

@js.native
@JSImport("snabbdom/modules/eventlisteners", JSImport.Namespace, globalFallback = "snabbdom_eventlisteners")
object SnabbdomEventListeners extends js.Object{
  val default: js.Any = js.native
}

@js.native
@JSImport("snabbdom/modules/attributes", JSImport.Namespace, globalFallback = "snabbdom_attributes")
object SnabbdomAttributes extends js.Object{
  val default: js.Any = js.native
}

// forked snabbdom-props module where the ground-thruth is the dom
@js.native
@JSImport("./snabbdom-custom-props", JSImport.Namespace)
object SnabbdomCustomProps extends js.Object {
  val default: js.Any = js.native
}

// original snabbdom-props
// @js.native
// @JSImport("snabbdom/modules/props", JSImport.Namespace, globalFallback = "snabbdom_props")
// object SnabbdomProps extends js.Object{
//   val default: js.Any = js.native
// }


@js.native
@JSImport("snabbdom/modules/style", JSImport.Namespace, globalFallback = "snabbdom_style")
object SnabbdomStyle extends js.Object {
  val default: js.Any = js.native
}