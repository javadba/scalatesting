package com.blazedb.spark

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * MiniSql
 *
 */

import breeze.linalg.*

import scala.language.implicitConversions
import scala.util.parsing.combinator.PackratParsers
import scala.util.parsing.combinator.lexical.StdLexical
import scala.util.parsing.combinator.syntactical.StandardTokenParsers
import scala.util.parsing.input.CharArrayReader.EofCh
import scala.util.parsing.combinator._


class MiniSqlParser {
//  extends RegexParsers with PackratParsers {
//
//  object Calculator extends RegexParsers {
//def number: Parser[Double] = """\d+(\.\d*)?""".r ^^ { _.toDouble }
//def factor: Parser[Double] = number | "(" ~> expr <~ ")"
//def term  : Parser[Double] = factor ~ rep( "*" ~ factor | "/" ~ factor) ^^ {
//  case number ~ list => (number /: list) {
//    case (x, "*" ~ y) => x * y
//    case (x, "/" ~ y) => x / y
//  }
//}
//def expr  : Parser[Double] = term ~ rep("+" ~ log(term)("Plus term") | "-" ~ log(term)("Minus term")) ^^ {
//  case number ~ list => list.foldLeft(number) { // same as before, using alternate name for /:
//    case (x, "+" ~ y) => x + y
//    case (x, "-" ~ y) => x - y
//  }
//}
//
//def apply(input: String): Double = parseAll(expr, input) match {
//  case Success(result, _) => result
//  case failure : NoSuccess => scala.sys.error(failure.msg)
//}
//}
//
//  type P = Parser[SqlStmt]
//
//  val AS = Keyword("AS")
//
//  val s : Parser[SqlStmt] = /* "select" <~ */ projs ~> " from " <~ sources
////  val s : Parser[SqlStmt] = "select" <~ projs ~> " from " <~ sources ~ (~> "where" ~ filters).? ~ (~> " group by " <~ groups).? ~ (~> "having" <~ having).? ~ (~> "order by" <~ ordering).?
//
//  val projs: Parser[SqlStmt] = (ident )
////  val projs: Parser[SqlStmt] = (ident (~> AS <~ ident).?) ~> ','.? ).* ^^ {
//
////  }
//  val sources: Parser[SqlStmt] = (ident (~> AS <~ ident).?).*
//
//  lazy val start: Parser[SqlStmt] = others
//
//  class SqlStmt {}
//
//  def parse(input: String): SqlStmt = {
//    // Initialize the Keywords.
//    initLexical
//    phrase(start)(new lexical.Scanner(input)) match {
//      case Success(plan, _) => plan
//      case failureOrError => sys.error(failureOrError.toString)
//    }
//  }
//
//  /* One time initialization of lexical.This avoid reinitialization of  lexical in parse method */
//  protected lazy val initLexical: Unit = lexical.initialize(reservedWords)
//
//  protected case class Keyword(str: String) {
//    def normalize: String = lexical.normalizeKeyword(str)
//    def parser: Parser[String] = normalize
//  }
//
//  protected implicit def asParser(k: Keyword): Parser[String] = k.parser
//
//  // By default, use Reflection to find the reserved words defined in the sub class.
//  // NOTICE, Since the Keyword properties defined by sub class, we couldn't call this
//  // method during the parent class instantiation, because the sub class instance
//  // isn't created yet.
//  protected lazy val reservedWords: Seq[String] =
//    this
//      .getClass
//      .getMethods
//      .filter(_.getReturnType == classOf[Keyword])
//      .map(_.invoke(this).asInstanceOf[Keyword].normalize)
//
//  private lazy val others: P =
//    wholeInput ^^ {
//      case input => fallback(input)
//    }
//
//    // Set the keywords as empty by default, will change that later.
//  override val lexical = new SqlLexical
//
//
//  // Returns the whole input string
//  protected lazy val wholeInput: Parser[String] = new Parser[String] {
//    def apply(in: Input): ParseResult[String] =
//      Success(in.source.toString, in.drop(in.source.length()))
//  }
//
//  // Returns the rest of the input string that are not parsed yet
//  protected lazy val restInput: Parser[String] = new Parser[String] {
//    def apply(in: Input): ParseResult[String] =
//      Success(
//        in.source.subSequence(in.offset, in.source.length()).toString,
//        in.drop(in.source.length()))
//  }
//}
//
//
//class SqlLexical extends StdLexical {
//  case class FloatLit(chars: String) extends Token {
//    override def toString: String = chars
//  }
//
//  /* This is a work around to support the lazy setting */
//  def initialize(keywords: Seq[String]): Unit = {
//    reserved.clear()
//    reserved ++= keywords
//  }
//
//  /* Normal the keyword string */
//  def normalizeKeyword(str: String): String = str.toLowerCase
//
//  delimiters += (
//    "@", "*", "+", "-", "<", "=", "<>", "!=", "<=", ">=", ">", "/", "(", ")",
//    ",", ";", "%", "{", "}", ":", "[", "]", ".", "&", "|", "^", "~", "<=>"
//  )
//
//  protected override def processIdent(name: String) = {
//    val token = normalizeKeyword(name)
//    if (reserved contains token) Keyword(token) else Identifier(name)
//  }
//
//  override lazy val token: Parser[Token] =
//    ( identChar ~ (identChar | digit).* ^^
//      { case first ~ rest => processIdent((first :: rest).mkString) }
//    | rep1(digit) ~ ('.' ~> digit.*).? ^^ {
//        case i ~ None => NumericLit(i.mkString)
//        case i ~ Some(d) => FloatLit(i.mkString + "." + d.mkString)
//      }
//    | '\'' ~> chrExcept('\'', '\n', EofCh).* <~ '\'' ^^
//      { case chars => StringLit(chars mkString "") }
//    | '"' ~> chrExcept('"', '\n', EofCh).* <~ '"' ^^
//      { case chars => StringLit(chars mkString "") }
//    | '`' ~> chrExcept('`', '\n', EofCh).* <~ '`' ^^
//      { case chars => Identifier(chars mkString "") }
//    | EofCh ^^^ EOF
//    | '\'' ~> failure("unclosed string literal")
//    | '"' ~> failure("unclosed string literal")
//    | delim
//    | failure("illegal character")
//    )
//
//  override def identChar: Parser[Elem] = letter | elem('_')
//
//  override def whitespace: Parser[Any] =
//    ( whitespaceChar
//    | '/' ~ '*' ~ comment
//    | '/' ~ '/' ~ chrExcept(EofCh, '\n').*
//    | '#' ~ chrExcept(EofCh, '\n').*
//    | '-' ~ '-' ~ chrExcept(EofCh, '\n').*
//    | '/' ~ '*' ~ failure("unclosed comment")
//    ).*
}

object MiniSqlParser {
  def main(args: Array[String]): Unit = {
    println("hello sql world")
  }}
